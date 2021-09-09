(ns basic-calculator.gui.key-bindings
  (:import
   (javax.swing Action JComponent KeyStroke)))

(def ^:private bindings
  {"0" ["0" "NUMPAD0"]
   "1" ["1" "NUMPAD1"]
   "2" ["2" "NUMPAD2"]
   "3" ["3" "NUMPAD3"]
   "4" ["4" "NUMPAD4"]
   "5" ["5" "NUMPAD5"]
   "6" ["6" "NUMPAD6"]
   "7" ["7" "NUMPAD7"]
   "8" ["8" "NUMPAD8"]
   "9" ["9" "NUMPAD9"]
   "." ["PERIOD" "DECIMAL"]
   "+" ["shift EQUALS" "PLUS" "ADD"]
   "\u2212" ["MINUS" "SUBTRACT"]
   "\u00D7" ["shift 8" "ASTERISK" "MULTIPLY"]
   "\u00F7" ["SLASH" "DIVIDE"]
   "CA" ["ESCAPE"]
   "%" ["shift 5"]
   "=" ["EQUALS" "ENTER"]})

(defn bind-keys! [pane & comps]
  (let [input-map (.getInputMap pane JComponent/WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        action-map (.getActionMap pane)]
    (doseq [[k v] bindings]
      (doseq [ks v]
        (.put input-map (KeyStroke/getKeyStroke ks) k)))

    (doseq [comp comps]
      (let [act (.getAction comp)
            key (str (.getValue act Action/NAME))]
        (.put action-map key act)))))
