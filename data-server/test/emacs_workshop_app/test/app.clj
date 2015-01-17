(ns emacs-workshop-app.test.app
  (:use clojure.test                    ; midje maybe?
        ring.mock.request
        emacs-workshop-app.handler))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) ""))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))
