import com.learndrools.domain.Applicant
import com.learndrools.domain.SuggestedRole
import com.learndrools.service.ApplicantService
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class ApplicantServiceIT extends Specification {

    def applicantService = new ApplicantService()

    def "suggestedRoleForApplicant"() {
        given:
        def suggestedRole = new SuggestedRole()

        when:
        suggestedRole = applicantService.suggestedRoleForApplicant(applicant,suggestedRole)

        then:
        suggestedRole.role == role

        where:
        applicant                              || role
        new Applicant("John", 32, 2500000, 11) || "Manager"
    }
}
