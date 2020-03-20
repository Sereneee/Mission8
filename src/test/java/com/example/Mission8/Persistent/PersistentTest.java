package com.example.Mission8.Persistent;

import com.example.Mission8.Model.Pet;
import com.example.Mission8.Repository.PetRepo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest

@ActiveProfiles("test")
public class PersistentTest { // this is aka repository test

    // declaration and instantiation of a variable that is used to signify successful C/U/D on db
    public static int oneInt = 1;
    public static String oneStr = "1";

    public  List<Pet> publicPetList = new ArrayList<Pet>() {
        {
            add(new Pet("100", "apples", 1, "dog"));
            add(new Pet("101", "apples", 1, "lion"));

        }
    };
    @Autowired
    private PetRepo petRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setTestData(){
        String crtTblQry = ("create table pet (" +
                "id varchar(30) NOT NULL," +
                "name varchar(30) NOT NULL," +
                "age int(10) NULL," +
                "type varchar(20) NULL," +
                "CONSTRAINT pet_pk PRIMARY KEY (id)" +
                ")");
        String insTblQry = "insert into pet (id, name, age, type) values" +
                "('" + publicPetList.get(0).getId() + "', '"
                + publicPetList.get(0).getName() + "', " +
                "" + publicPetList.get(0).getAge() + ", '" +
                "" + publicPetList.get(0).getType() + "');";
        String insTblQry2 = "insert into pet (id, name, age, type) values" +
                "('" + publicPetList.get(1).getId() + "', '"
                + publicPetList.get(1).getName() + "', " +
                "" + publicPetList.get(1).getAge() + ", '" +
                "" + publicPetList.get(1).getType() + "');";
        jdbcTemplate.execute(crtTblQry);
        jdbcTemplate.execute(insTblQry);
        jdbcTemplate.execute(insTblQry2);
    }
    @After
    public void deleteTestData(){
        String delTblQry = ("drop table pet");
        jdbcTemplate.execute(delTblQry);
    }

    // test create in persistent aka repo
    @Test
    public void createPersistentTest(){
        String id = "102", name = "bridget", type = "cat";
        int age = 2;
        Pet addedPet = new Pet(id, name, age, type);
        int result = petRepo.createPet(addedPet);
        assertEquals(oneInt, result);

        //read back what was added into db to see if it matches
        Pet readPet = petRepo.readById(id);

        assertEquals(addedPet.getId(), readPet.getId());
        assertEquals(addedPet.getName(), readPet.getName());
        assertEquals(addedPet.getAge(), readPet.getAge());
        assertEquals(addedPet.getType(), readPet.getType());
    }

    // test read all in persistent layer
    @Test
    public void readAllPersistentTest(){
        List<Pet>localPetList = new ArrayList<>();
        localPetList = petRepo.readAll();
        for(int i=0; i<publicPetList.size(); i++){
            assertEquals(publicPetList.get(i).getId(), localPetList.get(i).getId());
            assertEquals(publicPetList.get(i).getName(), localPetList.get(i).getName());
            assertEquals(publicPetList.get(i).getAge(), localPetList.get(i).getAge());
            assertEquals(publicPetList.get(i).getType(), localPetList.get(i).getType());
        }
    }

    // test read by id in persistent layer
    @Test
    public void readByIdPersistentTest(){
        Pet result = petRepo.readById(publicPetList.get(0).getId());
        assertEquals(publicPetList.get(0).getId(), result.getId());
        assertEquals(publicPetList.get(0).getName(), result.getName());
        assertEquals(publicPetList.get(0).getAge(), result.getAge());
        assertEquals(publicPetList.get(0).getType(), result.getType());
    }

    // test read by name and age in persistent layer
    @Test
    public void readByNameAndAgePersistentTest(){
        List<Pet>localPetList = new ArrayList<>();
        localPetList = petRepo.readByNameAndAge(publicPetList.get(0).getName(), Integer.toString(publicPetList.get(0).getAge()));
        for(int i=0; i<localPetList.size(); i++){
            assertEquals(publicPetList.get(i).getId(), localPetList.get(i).getId());
            assertEquals(publicPetList.get(i).getName(), localPetList.get(i).getName());
            assertEquals(publicPetList.get(i).getAge(), localPetList.get(i).getAge());
            assertEquals(publicPetList.get(i).getType(), localPetList.get(i).getType());
        }
    }

    // test read number of pets in db
    @Test
    public void countPersistentTest() throws Exception {
        int result = petRepo.countPet();
        assertEquals(publicPetList.size(), result);
    }

    // test update in persistent layer
    @Test
    public void updatePersistentTest() throws SQLException {
        Pet updatePet = new Pet ("100", "banana", 100, "crocodile");
        int result = petRepo.updatePet(publicPetList.get(0).getId(), updatePet);
        assertEquals(oneInt, result);

        // read back what was updated into db to see if it matches
        Pet readPet = petRepo.readById(publicPetList.get(0).getId());

        assertEquals(updatePet.getId(), readPet.getId());
        assertEquals(updatePet.getName(), readPet.getName());
        assertEquals(updatePet.getAge(), readPet.getAge());
        assertEquals(updatePet.getType(), readPet.getType());
    }

    // test delete in persistent layer
    @Test
    public void deletePersistentTest() throws SQLException {

        // modified method 1
        Pet readPetBeforeDelete = petRepo.readById(publicPetList.get(1).getId());
        assertNotNull(readPetBeforeDelete); // will be not null as it is not deleted yet

        int deleteResult = petRepo.deletePet(publicPetList.get(1).getId());
        assertEquals(oneInt, deleteResult);

        Pet readPetAfterDelete = petRepo.readById(publicPetList.get(1).getId());
        assertNull(readPetAfterDelete); // will be null as it was just deleted


//        // method 2
//        Pet pet = new Pet ("102", "banana", 100, "crocodile");
//        // insert pet into db first
//        int result = petRepo.createPet(pet);
//        assertEquals(oneInt, result);
//
//        // read pet that was just inserted
//        Pet readPet = petRepo.readById(pet.getId());
//        assertNotNull(readPet);
//
//        // delete the pet
//        int result2 = petRepo.deletePet(pet.getId());
//        assertEquals(oneInt, result2); // verify result
//        Pet readPet2 = petRepo.readById(pet.getId());
//        assertNull(readPet2); // verify that the pet isn't in db
    }
}
