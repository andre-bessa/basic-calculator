(ns basic-calculator.core)

(def max-digits 20)

(declare automaton calc-state)

(defn enter
  "Enter one `operand` and an `operation`.
   Supported operations:
     :basic-calculator.core/addition
     :basic-calculator.core/subtraction
     :basic-calculator.core/multiplication
     :basic-calculator.core/division
     :basic-calculator.core/equals
     :basic-calculator.core/percentage"
  [operand operation]
  (swap! calc-state automaton (bigdec operand) operation))

(defn result
  "Gets the result of last calculation."
  []
  (:register @calc-state))

(defn clear-all
  "Clear all saved results."
  []
  (reset! calc-state {:operator nil
                      :register 0M}))

(def ^:private calc-state
  (atom {:operator nil
         :register 0M}))

(defn state [] @calc-state)

(def ^:private operator-implementations
  {::addition +
   ::subtraction -
   ::multiplication *
   ::division /})

(defn- compute [operation operand1 operand2]
  (let [operator-impl (get operator-implementations operation)]
    #_{:clj-kondo/ignore [:unresolved-symbol]}
    (with-precision max-digits :rounding HALF_EVEN
                    (operator-impl operand1 operand2))))

(defn- percentage [operation operand1 operand2]
  (let [percentage (compute ::division operand2 100)
        percentage-of (compute ::multiplication operand1 percentage)]
    (if (or (= operation ::addition)
            (= operation ::subtraction))
      (compute operation operand1 percentage-of)
      (compute operation operand1 percentage))))

(def ^:private automaton
  (letfn [(initial-state
            [calc-state num op]
            (if (nil? (:operator calc-state))
              (no-operator-state calc-state num op)
              (operator-state calc-state num op)))
          (no-operator-state
            [calc-state num op]
            (case op
              ::equals (recur {:operator nil :register num}
                              nil
                              nil)
              ::percentage (recur {:operator nil :register (compute ::division num 100)}
                                  nil
                                  nil)
              nil calc-state
              (operator-state {:operator op :register num}
                              nil
                              nil)))
          (operator-state
            [{:keys [operator register] :as calc-state} num op]
            (case op
              ::equals (no-operator-state {:operator nil :register (compute operator register num)}
                                          nil
                                          nil)
              ::percentage (no-operator-state {:operator nil :register (percentage operator register num)}
                                              nil
                                              nil)
              nil calc-state
              (recur {:operator op :register (compute operator register num)}
                     nil
                     nil)))]
    initial-state))
