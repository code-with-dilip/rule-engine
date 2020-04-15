import com.learndrools.domain.Bonus
import com.learndrools.domain.Employee
import com.learndrools.service.BonusService
import spock.lang.Specification

class BonusServiceSpecIT extends Specification {

    def bonusService = new BonusService()

  /*  void setup() {
        System.set
    }*/

    def "applyBonus"() {

        given:

        when:
        def bonus = bonusService.applyBonus(employee, new Bonus())

        then:
        bonus.bonusAmount == bonusValue
        def pointsMap = bonus.pointsMap
        pointsMap.size() ==2
        println("pointsMap : $pointsMap")


        where:
        employee                                    || bonusValue
        new Employee("Ian", 100000, "Developer", 4) || employee.salary * 0.3

    }
}
