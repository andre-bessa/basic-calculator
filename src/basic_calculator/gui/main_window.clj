(ns basic-calculator.gui.main-window
  (:require
   [basic-calculator.core :as calculator]
   [basic-calculator.gui.display :as display]
   [basic-calculator.gui.key-bindings :refer [bind-keys!]]
   [basic-calculator.gui.utils :refer [button gridbag-layout]])
  (:import
   (javax.swing JFrame JPanel SwingUtilities))
  (:gen-class))

(defn- enter
  "Retrieve display value, enter value and operation in the calculator and update display."
  [operation]
  (calculator/enter (display/get-value) operation)
  (display/update!))

(defn- gui []
  (let [digits (->> ["0" "1" "2" "3" "4" "5" "6" "7" "8" "9" "."]
                    (map #(button % (display/insert %)))
                    (into []))
        decimal-sep-btn (digits 10)
        add-btn (button "+" (enter ::calculator/addition))
        sub-btn (button "\u2212" (enter ::calculator/subtraction))
        mul-btn (button "\u00D7" (enter ::calculator/multiplication))
        div-btn (button "\u00F7" (enter ::calculator/division))
        eq-btn (button "=" (enter ::calculator/equals))
        percentage-btn (button "%" (enter ::calculator/percentage))
        ca-btn (button "CA"
                       (calculator/clear-all)
                       (display/clear))
        sign-btn (button "\u00B1"
                         (-> (display/get-value)
                             (* -1)
                             str
                             display/show))
        frame (JFrame. "Basic Calculator")
        pane (JPanel.)]

    (apply bind-keys!
           pane
           add-btn
           sub-btn
           mul-btn
           div-btn
           eq-btn
           percentage-btn
           ca-btn
           sign-btn
           digits) ; decimal-sep-btn included with digits.

    (gridbag-layout
     pane
     ;; Row 0
     display/text-field 0 0 5 1
     ;; Row 1
     (digits 7) 1 0 1 1
     (digits 8) 1 1 1 1
     (digits 9) 1 2 1 1
     div-btn    1 3 1 1
     ca-btn     1 4 1 1
     ;; Row 2
     (digits 4) 2 0 1 1
     (digits 5) 2 1 1 1
     (digits 6) 2 2 1 1
     mul-btn    2 3 1 1
     sign-btn   2 4 1 1
     ;; Row 3
     (digits 1) 3 0 1 1
     (digits 2) 3 1 1 1
     (digits 3) 3 2 1 1
     sub-btn    3 3 1 1
     eq-btn     3 4 1 2
     ;; Row 4
     (digits 0)      4 0 1 1
     decimal-sep-btn 4 1 1 1
     percentage-btn  4 2 1 1
     add-btn         4 3 1 1)

    (doto frame
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE) ; Comment to use REPL.
      (.setContentPane pane)
      (.setSize 330 480)
      (.setLocationRelativeTo nil)
      (.setVisible true))))

(defn -main
  [& _]
  (SwingUtilities/invokeLater gui))
