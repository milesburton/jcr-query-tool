package mb.usecase

import mb.jcrquery.NodeIteratorConsumer

import javax.jcr.NodeIterator

class NodeToFileConsumer implements NodeIteratorConsumer{

    File outputFile

    @Override
    void consume(NodeIterator nodeIterator) {

        nodeIterator.each { javax.jcr.Node node ->
	    def path = node.path.replace(' ','%20')
            outputFile.append path + '\n'
        }

    }
}
