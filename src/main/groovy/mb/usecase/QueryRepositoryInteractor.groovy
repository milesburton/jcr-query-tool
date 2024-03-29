package mb.usecase

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import mb.jcrquery.CRXQuery
import mb.jcrquery.JcrRepositoryFactory
import mb.jcrquery.NodeIteratorConsumer

import javax.jcr.NodeIterator
import javax.jcr.Repository
import javax.jcr.Session
import javax.jcr.SimpleCredentials
import javax.jcr.query.Query
import javax.jcr.query.QueryManager
import javax.jcr.query.QueryResult

@Component
class QueryRepositoryInteractor {

    @Autowired
    JcrRepositoryFactory jcrRepositoryFactory

    void queryJcr(CRXQuery query, NodeIteratorConsumer nodeIteratorConsumer){

        Repository r = jcrRepositoryFactory.fetchRepository(query.crxUri)
        SimpleCredentials creds = new SimpleCredentials(query.username, query.password.toCharArray())

		Session session = null
        try {
            session = r.login(creds, "crx.default");
            QueryManager qm = session.getWorkspace().getQueryManager()

            Query q = qm.createQuery(query.xpath, Query.XPATH)

            QueryResult result = q.execute();
            NodeIterator it = result.getNodes();

            nodeIteratorConsumer.consume(it)

            session.logout()
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.toString());
        } finally {
			if(session) session.logout()
		}

    }
}
