(set-env!
  :project 'hoplon-chartis-example
  :version "0.1.0"
  :dependencies
  '[[castra "3.0.0-SNAPSHOT"]
    [hoplon/boot-hoplon "0.1.7"]
    [hoplon "6.0.0-alpha10"]
    [adzerk/boot-reload "0.3.2"]
    [pandeiro/boot-http "0.6.3"]
    [org.clojure/clojurescript "1.7.122"]
    [adzerk/boot-cljs "1.7.48-3"]
    [cljsjs/boot-cljsjs "0.5.0" :scope "test"]
    [exicon/semantic-ui "2.0.6-SNAPSHOT"]
    [com.andrewmcveigh/cljs-time "0.3.6"]
    [cljsjs/c3 "0.4.10-0"]
    ]
  :source-paths #{"src"}
  :resource-paths #{"assets"})

(require
  '[hoplon.boot-hoplon :refer [hoplon prerender html2cljs]]
  '[adzerk.boot-reload :refer [reload]]
  '[pandeiro.boot-http :refer [serve]]
  '[adzerk.boot-cljs :refer [cljs]]
  '[cljsjs.boot-cljsjs :refer [from-cljsjs]])

(task-options!
  speak {:theme "woodblock"}
  cljs {:compiler-options {:pseudo-names true}})

(deftask dev
  "Build Appboard for development."
  []
  (comp
    (serve :port 3109)
    (from-cljsjs :profile :development)
    (sift :to-resource #{#"themes"})
    (sift :to-resource #{#"semantic-ui.inc.css"})
    (sift :move {#"^semantic-ui.inc.css$" "semantic-ui.css"})
    (watch)
    (hoplon :pretty-print true)
    (reload)
    (cljs :optimizations :none
          :source-map true)
    (speak)))

(deftask prod
  "Build Appboard for production."
  []
  (comp
    (from-cljsjs :profile :production)
    (sift :to-resource #{#"themes"})
    (sift :to-resource #{#"semantic-ui.min.inc.css"})
    (sift :move {#"^semantic-ui.min.inc.css$" "semantic-ui.css"})
    (hoplon)
    (cljs :optimizations :advanced)
    (sift :invert true :include #{#"^out/"})))
