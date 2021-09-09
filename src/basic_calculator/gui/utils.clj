(ns basic-calculator.gui.utils
  (:import
   (java.awt Font GridBagConstraints GridBagLayout)
   (javax.swing AbstractAction JButton)))

(defmacro button
  "Create a JButton with an Action.
   `name`: Action NAME and button text.
   `forms`: Code that will be executed by the actionPerformed."
  [name & forms]
  `(let [act# (proxy [AbstractAction] [~name]
                (actionPerformed [event#]
                  ~@forms))]
     (doto (JButton. act#)
       (.setFont (Font. Font/SANS_SERIF Font/BOLD 14))
       (.setFocusPainted false))))

(defmacro gridbag-layout
  "Sets the `pane` laytout to a new GridBagLayout and adds every component to it.
   `args`: (component row column width height)+"
  [pane & args]
  {:pre [(zero? (rem (count args) 5))]}
  (let [constraint (gensym "constraint")
        layout (gensym "layout")
        panel (gensym "panel")]
    `(let [~constraint (GridBagConstraints.)
           ~layout (GridBagLayout.)
           ~panel ~pane]
       (.setLayout ~panel ~layout)
       (set! (.weightx ~constraint) 1)
       (set! (.weighty ~constraint) 1)
       (set! (.fill ~constraint) GridBagConstraints/BOTH)
       ~@(loop [forms '()
                [comp row coll width height & more] args]
           (if (nil? comp)
             forms
             (recur
              (concat forms
                      `((set! (.gridx ~constraint) ~coll)
                        (set! (.gridy ~constraint) ~row)
                        (set! (.gridwidth ~constraint) ~width)
                        (set! (.gridheight ~constraint) ~height)
                        (.setConstraints ~layout ~comp ~constraint)
                        (.add ~panel ~comp)))
              more))))))
