(defproject basic-calculator "0.1.0"
  :description "Basic Calculator with GUI"
  :url "https://github.com/andre-bessa/basic-calculator"
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :main ^:skip-aot basic-calculator.gui.main-window
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
