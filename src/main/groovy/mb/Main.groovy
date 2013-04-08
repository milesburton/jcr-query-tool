package mb

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.stereotype.Component
import mb.argumentprocessing.ArgumentsMerge
import mb.jcrquery.CRXQuery
import mb.jcrquery.NodeIteratorConsumer
import mb.usecase.ArgumentsValidatorInteractor
import mb.usecase.NodeToFileConsumer
import mb.usecase.QueryRepositoryInteractor


@Component
class Main {

    @Autowired
    ArgumentsValidatorInteractor argumentsValidatorInteractor

    @Autowired
    ArgumentsMerge argumentsMerge

    @Autowired
    QueryRepositoryInteractor queryRepositoryInteractor

    static void main(String[] args) {

        def defaultArguments = [
                'crx': [required: false, defaultValue: 'http://test-publisher01.wgsn.com:7503/crx/server'],
                'username': [required: false, defaultValue: 'admin'],
                'password': [required: true],
                'outputfile': [required: false, defaultValue: 'xpath.result'],
                'xpath': [required: true]
        ]

        ApplicationContext ctx =
            new AnnotationConfigApplicationContext("mb");

        Main main = ctx.getBean(Main.class);

        main.run(defaultArguments, args.toList())

    }

    void run(Map defaultArguments, List<String> arguments) {

        def errorOutputter = { println it }

        if (argumentsValidatorInteractor.isValid(errorOutputter, defaultArguments, arguments)) {

            Map settingsMap = argumentsMerge.merge(defaultArguments, arguments)

            println arguments
            println settingsMap

            def outputFile = new File(settingsMap.outputfile.toString())

            if (outputFile.exists()) {
                outputFile.delete()
            }

            if (!outputFile.createNewFile()){
                println "Failed to create file"
            }

            CRXQuery q = new CRXQuery(
                    crxUri: settingsMap.crx,
                    username: settingsMap.username,
                    password: settingsMap.password,
                    xpath: settingsMap.xpath
            )

            println q

            NodeIteratorConsumer nodeToFileConsumer = new NodeToFileConsumer(outputFile: outputFile)

            queryRepositoryInteractor.queryJcr(q, nodeToFileConsumer)
        }

        println "Finished"
    }


}
