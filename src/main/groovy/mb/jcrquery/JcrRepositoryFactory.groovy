package mb.jcrquery

import org.apache.jackrabbit.commons.JcrUtils
import org.springframework.stereotype.Component

import javax.jcr.Repository

@Component
class JcrRepositoryFactory {

    Repository fetchRepository(String uri){
        JcrUtils.getRepository(uri);
    }
}
