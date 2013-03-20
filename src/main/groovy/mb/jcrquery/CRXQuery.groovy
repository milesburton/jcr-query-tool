package mb.jcrquery

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
class CRXQuery {

    String crxUri
    String username
    String password
    String xpath
}
