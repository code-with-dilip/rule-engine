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
        def startTime = System.currentTimeMillis()
        suggestedRole = applicantService.suggestedRoleForApplicant(applicant,suggestedRole)
        println("Completion time : ${(System.currentTimeMillis() - startTime)}")
        then:
        suggestedRole.role == role

        where:
        applicant                              || role
        new Applicant("John", 32, 2500000, 11) || "Manager"
        new Applicant("John Jr", 32, 510000, 9) || "Senior Developer"
        new Applicant("John Jr 1", 3, 210000, 4) || "Developer"
    }

    def "loadRulesFromClassPath"() {
        given:
        def suggestedRole = new SuggestedRole()
        //suggestedRole.role="abc"
        when:
        def startTime = System.currentTimeMillis()
        suggestedRole = applicantService.loadRulesFromClassPath(applicant,suggestedRole)
        println("Completion time : ${(System.currentTimeMillis() - startTime)}")
        then:
        suggestedRole.role == role

        where:
        applicant                              || role
        new Applicant("John", 32, 2500000, 11) || "Manager"
        new Applicant("John Jr", 32, 510000, 9) || "Senior Developer"
        new Applicant("John Jr 1", 3, 210000, 4) || "Developer"
    }

    def "loadRulesFromClassPath_Stateless"() {
        given:
        def suggestedRole = new SuggestedRole()
        suggestedRole.role="abc"
        when:
        def startTime = System.currentTimeMillis()
        suggestedRole = applicantService.loadRulesFromClassPath_Stateless(applicant,suggestedRole)
        println("Completion time : ${(System.currentTimeMillis() - startTime)}")
        then:
        suggestedRole.role == role

        where:
        applicant                              || role
        new Applicant("John", 32, 2500000, 11) || "Manager"
        new Applicant("John Jr", 32, 510000, 9) || "Senior Developer"
        new Applicant("John Jr 1", 3, 210000, 4) || "Developer"
    }

    def "suggestedRoleForApplicantUsingOwnLogic"() {

        when:
        def startTime = System.currentTimeMillis()
        def suggestedRole = applicantService.suggestedRoleForApplicantUsingOwnLogic(applicant)
        println("Completion time : ${(System.currentTimeMillis() - startTime)}")

        then:
        suggestedRole!=null
        suggestedRole.role == role

        where:
        applicant                              || role
        new Applicant("John", 32, 2500000, 11) || "Manager"
        new Applicant("John Jr", 32, 510000, 9) || "Senior Developer"
        new Applicant("John Jr 1", 3, 210000, 4) || "Developer"
    }
}
