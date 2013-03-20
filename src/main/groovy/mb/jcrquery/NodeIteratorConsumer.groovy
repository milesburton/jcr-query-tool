package mb.jcrquery

import javax.jcr.NodeIterator

interface NodeIteratorConsumer {

    void consume(NodeIterator nodeIterator)
}
