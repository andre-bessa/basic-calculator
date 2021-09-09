(ns basic-calculator.gui.display
  (:require
   [basic-calculator.core :as calculator :refer [max-digits]])
  (:import
   (java.awt Font)
   (java.text DecimalFormat)
   (javax.swing JTextField)))

(def ^:private fmt
  (doto (DecimalFormat.)
    (.setParseBigDecimal true)
    (.setRoundingMode java.math.RoundingMode/HALF_EVEN)))

(def ^:private showing? (atom true))

(def text-field
  (doto (JTextField. "0" max-digits)
    (.setFont (Font. Font/MONOSPACED Font/BOLD 18))
    (.setHorizontalAlignment JTextField/RIGHT)
    (.setEditable false)))

(defn get-value []
  (.parse fmt (.getText text-field)))

(defn show [txt]
  (.setText text-field txt))

(defn show! [txt]
  (reset! showing? true)
  (show txt))

(defn update! []
  (let [{:keys [register operator]} (calculator/state)]
    (case operator
      ::calculator/addition (show! (str register " +"))
      ::calculator/subtraction (show! (str register " \u2212"))
      ::calculator/multiplication (show! (str register " \u00D7"))
      ::calculator/division (show! (str register " \u00F7"))
      (show (str register)))))

(defn clear []
  (reset! showing? true)
  (.setText text-field "0"))

(defn insert [txt]
  (let [current-txt (.getText text-field)
        new-txt (if @showing?
                  (do
                    (reset! showing? false)
                    (if (= txt ".")
                      "0."
                      txt))
                  (str current-txt txt))]
    (.setText text-field
              (if (> (count new-txt) max-digits)
                (subs new-txt 0 max-digits)
                new-txt))))
