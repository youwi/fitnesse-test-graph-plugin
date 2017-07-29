# fitnesse-test-graph-plugin
build a chart on
        
        http://127.0.0.1:9090/yourWiki?testHistoryGraph

##How to use
mkdir "plugins" beside fitnesse.jar then restart fitnesse

        .
        ├── FitNesseRoot
        │   └── *****
        ├── plugins
        │   └── plugin.testGraph-20170720.jar
        └── fitnesse-standalone.jar 

##Preview

![Preview](https://github.com/youwi/fitnesse-test-graph-plugin/raw/master/doc/preview.png)
           
           
##Source
        .
        ├── README.md
        ├── build
        │   ├── classes
        │   │   └── java
        │   │       └── main
        │   │           └── fitnesse
        │   │               └── plugin
        │   │                   └── graph
        │   │                       ├── FitnesseWillLoadMe.class
        │   │                       ├── testHistoryGraphResponder.class
        │   │                       └── testHistoryJsonResponder.class
        │   ├── libs
        │   │   └── pluginTestGraph-20170720.jar
        │   ├── resources
        │   │   └── main
        │   │       ├── META-INF
        │   │       │   └── services
        │   │       │       └── fitnesse.plugins.PluginFeatureFactory
        │   │       ├── fitnesse
        │   │       │   └── resources
        │   │       │       └── templates
        │   │       │           └── testHistoryGraph.vm
        │   │       └── fitnesse.resources.javascript
        │   │           └── echarts.min.js
        │   └── tmp
        │       ├── compileJava
        │       └── jar
        │           └── MANIFEST.MF
        ├── build.gradle
        ├── doc
        │   └── preview.png
        └── src
            ├── main
            │   ├── java
            │   │   └── fitnesse
            │   │       └── plugin
            │   │           └── graph
            │   │               ├── FitnesseWillLoadMe.java
            │   │               ├── testHistoryGraphResponder.java
            │   │               └── testHistoryJsonResponder.java
            │   └── resources
            │       ├── META-INF
            │       │   └── services
            │       │       └── fitnesse.plugins.PluginFeatureFactory
            │       ├── fitnesse
            │       │   └── resources
            │       │       └── templates
            │       │           └── testHistoryGraph.vm
            │       └── fitnesse.resources.javascript
            │           └── echarts.min.js
            └── test
                └── java
                    └── fitnesse
                        └── plugin
                            └── graph
                                └── testHistoryGraphResponderTest.java
