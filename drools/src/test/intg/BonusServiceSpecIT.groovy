import com.learndrools.domain.Bonus
import com.learndrools.domain.Employee
import com.learndrools.domain.Hobby
import com.learndrools.service.BonusService
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class BonusServiceSpecIT extends Specification {

    def bonusService = new BonusService()

    def "applyBonus"() {

        given:

        when:
        def bonus = bonusService.applyBonus(employee, new Bonus())

        then:
        bonus.bonusAmount == bonusValue
        def pointsMap = bonus.pointsMap
        pointsMap.size() == 1
        println("pointsMap : $pointsMap")


        where:
        employee                                                                                                      || bonusValue
        new Employee("Ian", 100000, "Developer", 4.1, "coaching", List.of(new Hobby("gaming"), new Hobby("cricket"))) || employee.salary * 0.4
        new Employee("Ian", 100000, "Developer", 4.1, "", List.of(new Hobby("basketball"), new Hobby("volleyball")))  || employee.salary * 0.3
        new Employee("Ian", 100000, "Developer", 4.1, "coaching", List.of(new Hobby("gaming"), new Hobby("cricket"))) || employee.salary * 0.4

    }
}
