package com.greglturnquist.payroll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @DisplayName("Testing the setter of the employee' id")
    @Test
    public void assertValidSetID(){
        //Arrange
        Employee emp1 = new Employee("Marta", "Lopes", "no", 2,
                "email@email.com");
        emp1.setId(12342L);
        Long expected = 222222L;
        emp1.setId(222222L);
        //Act
        Long result = emp1.getId();
        //Assert
        Assertions.assertEquals(expected, result);
    }

    @DisplayName("Testing the getter of the employee' id")
    @Test
    public void assertValidGetID(){
        //Arrange
        Employee emp1 = new Employee("Marta", "Lopes", "no", 2,
                "email@email.com");
        emp1.setId(12345L);
        Long expected = 12345L;
        //Act
        Long result = emp1.getId();
        //Assert
        Assertions.assertEquals(expected, result);
    }

    @DisplayName("Testing the getter of the employee' id when comparing different ids")
    @Test
    public void assertFalseGetID(){
        //Arrange
        Employee emp1 = new Employee("Marta", "Lopes", "no", 2,
                "email@email.com");
        emp1.setId(12342L);
        Long expected = 12345L;
        //Act
        Long result = emp1.getId();
        //Assert
        Assertions.assertNotEquals(expected, result);
    }

    @DisplayName("Testing Exception thrown: invalid first name")
    @Test
    public void assertInvalidStatementWhileSettingFirstName(){
        //Arrange
        Employee emp1 = new Employee("LOL", "Sim", "Não", 6,
                "email@email.com");
        emp1.setFirstName(null);
        //Act
        String expected = emp1.getFirstName();
        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(expected, "Sim", "Não",
                6, "email@email.com"));
    }

    @DisplayName("Testing the setter of the employee' first name")
    @Test
    public void assertValidSetFirstName(){
        //Arrange
        Employee emp1 = new Employee("Marta", "Lopes", "no", 2,
                "email@email.com");
        emp1.setFirstName("Sim");
        String expected = "Sim";
        //Act
        String result = emp1.getFirstName();
        //Assert
        Assertions.assertEquals(expected, result);
    }


    @DisplayName("Testing the setter of the employee' first name when comparing two different last names")
    @Test
    public void comparingTwoDifferentLastNames(){
        //Arrange
        Employee emp1 = new Employee("Marta", "Lopes", "no", 2,
                "email@email.com");
        emp1.setFirstName("Sim");
        String expected = "Marta";
        //Act
        String result = emp1.getFirstName();
        //Assert
        Assertions.assertNotEquals(expected, result);
    }

    @DisplayName("Testing the setter of the employee' last name")
    @Test
    public void assertValidSetLastName(){
        //Arrange
        Employee emp1 = new Employee("Marta", "Lopes", "no", 2,
                "email@email.com");
        emp1.setLastName("What");
        String expected = "What";
        //Act
        String result = emp1.getLastName();
        //Assert
        Assertions.assertEquals(expected, result);
    }

    @DisplayName("Testing the setter of the employee' last name")
    @Test
    public void cpmparingTwoDifferentLastNames(){
        //Arrange
        Employee emp1 = new Employee("Marta", "Lopes", "no", 2,
                "email@email.com");
        emp1.setLastName("What");
        String expected = "Lopes";
        //Act
        String result = emp1.getLastName();
        //Assert
        Assertions.assertNotEquals(expected, result);
    }

    @DisplayName("Testing Exception thrown: invalid last name")
    @Test
    public void assertInvalidStatementWhileSettingLastName(){
        //Arrange
        Employee emp1 = new Employee("LOL", "Sim", "Não", 6,
                "email@email.com");
        emp1.setLastName(null);
        //Act
        String expected = emp1.getLastName();
        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee(expected, "Sim", "Não",
                6, "email@email.com"));
    }


    @DisplayName("Testing the setter of the employee' description when comparing two different description")
    @Test
    public void comparingTwoDifferentDescription(){
        //Arrange
        Employee emp1 = new Employee("Marta", "Lopes", "Uma descrição", 2,
                "email@email.com");
        emp1.setDescription("Não gosto");
        String expected = "Uma descrição";
        //Act
        String result = emp1.getDescription();
        //Assert
        Assertions.assertNotEquals(expected, result);
    }

    @DisplayName("Testing the setter of the employee' last name")
    @Test
    public void assertValidDescription(){
        //Arrange
        Employee emp1 = new Employee("Marta", "Lopes", "no", 2,
                "email@email.com");
        emp1.setLastName("What");
        String expected = "What";
        //Act
        String result = emp1.getLastName();
        //Assert
        Assertions.assertEquals(expected, result);
    }

    @DisplayName("Testing Exception thrown: invalid description")
    @Test
    public void assertInvalidStatementWhileSettingDescription(){
        //Arrange
        Employee emp1 = new Employee("LOL", "Sim", "Não", 6,
                "email@email.com");
        emp1.setDescription(null);
        //Act
        String expected = emp1.getDescription();
        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee("LOL", "Sim", expected,
                6, "email@email.com"));
    }

    @DisplayName("Testing Exception thrown: invalid description")
    @Test
    public void shouldThrowException_EmptyDescription(){
        //Arrange
        Employee emp1 = new Employee("Senhor", "Dias", "Empregado de mesa", 50,
                "email@email.com");
        emp1.setDescription(" ");
        //Act
        String expected = emp1.getDescription();
        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee("Senhor", "Dias", expected,
                50, "email@email.com"));
    }


    @DisplayName("Testing the setter of the employee' job years when comparing two different job years")
    @Test
    public void comparingTwoDifferentJobYears(){
        //Arrange
        Employee emp1 = new Employee("Marta", "Lopes", "Uma descrição", 2,
                "email@email.com");
        emp1.setJobYears(1);
        int expected = 2;
        //Act
        int result = emp1.getJobYears();
        //Assert
        Assertions.assertNotEquals(expected, result);
    }

    @DisplayName("Testing the setter of the employee' job years")
    @Test
    public void assertValidJobYears(){
        //Arrange
        Employee emp1 = new Employee("Marta", "Lopes", "no", 2,
                "email@email.com");
        emp1.setJobYears(10);
        int expected = 10;
        //Act
        int result = emp1.getJobYears();
        //Assert
        Assertions.assertEquals(expected, result);
    }

    @DisplayName("Testing Exception thrown: invalid job years")
    @Test
    public void assertInvalidStatementWhileSettingJobYears(){
        //Arrange
        Employee emp1 = new Employee("LOL", "Sim", "Não", 6,
                "email@email.com");
        emp1.setJobYears(0);
        //Act
        int expected = emp1.getJobYears();
        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee("LOL", "Sim", "Não",
                expected, "email@email.com"));
    }

    @DisplayName("Testing Exception thrown: invalid job years")
    @Test
    public void assertInvalidStatementWhileSettingJobYearsAbove80(){
        //Arrange
        Employee emp1 = new Employee("LOL", "Sim", "Não", 80,
                "email@email.com");
        emp1.setJobYears(81);
        //Act
        int expected = emp1.getJobYears();
        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee("LOL", "Sim", "Não",
                expected, "email@email.com"));
    }

    @DisplayName("Testing Exception thrown: invalid job years")
    @Test
    public void assertInvalidStatementWhileSettingNegativeJobYears(){
        //Arrange
        Employee emp1 = new Employee("LOL", "Sim", "Não", 1,
                "email@email.com");
        emp1.setJobYears(-1);
        //Act
        int expected = emp1.getJobYears();
        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee("LOL", "Sim", "Não",
                expected, "email@email.com"));
    }

    @DisplayName("Testing the setter of the employee's email")
    @Test
    public void assertValidEmail(){
        //Arrange
        Employee emp1 = new Employee("Marta", "Lopes", "A description", 12,
                "email@email.com");
        emp1.setEmail("marta.lopes@hotmail.com");
        String expected = "marta.lopes@hotmail.com";
        //Act
        String result = emp1.getEmail();
        //Assert
        Assertions.assertEquals(expected, result);
    }

    @DisplayName("Testing Exception thrown: null email")
    @Test
    public void shouldThrowExceptionDueToInvalidEmail(){
        //Arrange
        Employee emp1 = new Employee("Ana", "Maria", "Copywriter", 1,
                "ana@maria.pt");
                //Act
        emp1.setEmail(null);
        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee("Ana", "Maria",
                "Copywriter", 1, emp1.getEmail()));
    }

    @DisplayName("Testing Exception thrown: invalid email that doesn't contain '@'")
    @Test
    public void shouldThrowExceptionDueToIncorrectEmail(){
        //Arrange
        Employee emp1 = new Employee("Joana", "Faria", "Developer", 3,
                "joana@faria.pt");
        //Act
        emp1.setEmail("joana.faria.pt");
        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Employee("Joana", "Faria",
                "Developer", 3, emp1.getEmail()));
    }
}