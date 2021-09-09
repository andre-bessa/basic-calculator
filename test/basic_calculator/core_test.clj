(ns basic-calculator.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [basic-calculator.core :as calculator]))

(deftest basic-operations
  (testing "Addition"
    (calculator/enter 1000 ::calculator/addition)
    (calculator/enter 10 ::calculator/equals)
    (is (= 1010M (calculator/result)) "1000 + 10 = 1010"))

  (testing "Subtraction"
    (calculator/enter 1000 ::calculator/subtraction)
    (calculator/enter 10 ::calculator/equals)
    (is (= 990M (calculator/result)) "1000 - 10 = 990"))

  (testing "Multiplication"
    (calculator/enter 1000 ::calculator/multiplication)
    (calculator/enter 10 ::calculator/equals)
    (is (= 10000M (calculator/result)) "1000 * 10 = 10000"))

  (testing "Division"
    (calculator/enter 1000 ::calculator/division)
    (calculator/enter 10 ::calculator/equals)
    (is (= 100M (calculator/result)) "1000 / 10 = 100")))

(deftest chained-operations
  (testing "Addition > Multiplication > Subtraction > Division > Division"
    (calculator/enter 10 ::calculator/addition)
    (calculator/enter 10 ::calculator/multiplication)
    (calculator/enter 5 ::calculator/subtraction)
    (calculator/enter 8 ::calculator/division)
    (calculator/enter 2 ::calculator/division)
    (calculator/enter 2 ::calculator/equals)
    (is (= 23M (calculator/result)) "((10 + 10) * 5 - 8) / 2 / 2 = 23")))

(deftest reuse-result-after-pressing-equals
  (calculator/enter 5 ::calculator/addition)
  (calculator/enter 5 ::calculator/equals)
  (calculator/enter (calculator/result) ::calculator/addition)
  (calculator/enter 5 ::calculator/equals)
  (is (= 15M (calculator/result))))

(deftest percentages
  (testing "Addition"
    (calculator/enter 1000 ::calculator/addition)
    (calculator/enter 10 ::calculator/percentage)
    (is (= 1100M (calculator/result)) "1000 + 10% = 1100"))

  (testing "Subtraction"
    (calculator/enter 1000 ::calculator/subtraction)
    (calculator/enter 10 ::calculator/percentage)
    (is (= 900M (calculator/result)) "1000 - 10% = 900"))

  (testing "Multiplication"
    (calculator/enter 1000 ::calculator/multiplication)
    (calculator/enter 10 ::calculator/percentage)
    (is (= 100M (calculator/result)) "1000 * 10% = 100"))

  (testing "Division"
    (calculator/enter 1000 ::calculator/division)
    (calculator/enter 10 ::calculator/percentage)
    (is (= 10000M (calculator/result)) "1000 / 10% = 10000")))
