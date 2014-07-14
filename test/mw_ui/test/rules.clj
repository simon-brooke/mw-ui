(ns mw-ui.test.rules
  (:use clojure.test
        ring.mock.request
        mw-ui.routes.rules))

;; Test file specifically to test the helper function in rules handler
(deftest test-rules-processor
  (testing "Rules processor"
    (let [request {:src "if state is new and altitude is less than 10 then state should be water"}
          response (process-rules-request request)]
      (is (= (:message response) "Successfully compiled 1 rules"))
      (is (= (count (:rules response)) 1))
      (is (= (:rule-text response) (:src request))))))
