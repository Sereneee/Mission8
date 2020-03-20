package com.example.Mission8.Controller;

import com.example.Mission8.Model.Pet;
import com.example.Mission8.Service.PetService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RefreshScope
@Log4j2
@EnableAutoConfiguration
@RestController // specialized version of controller (@Controller + @ResponseBody)
// @Controller is used to mark a class as a web request handler
@RequestMapping("/Pet")

public class PetController {
    @Value("${spring.profiles.active.message}")
    private String message;
    @RequestMapping("/hello")
    @ResponseBody
    String sayHello()
    {
        return(message);
    };

    @Autowired
    PetService petService; // creating an instance of PetService so that its functions can be used

    /* create pet */
    @PostMapping
    public int create(@RequestHeader("userId") Integer userID, @RequestBody Pet pet) {
        log.info("User with ID:" + userID + " has just accessed create pet function");
        return petService.createPet(pet);
    }

    /* read all pet */
    @GetMapping
    //@ResponseBody  - no need this when there is @RestController written on top
    public List<Pet> readAllPets(@RequestHeader("userId") Integer userID) {
        log.trace("User with ID:" + userID + " has just accessed readAllPets function");
        return petService.readAll();
    }

    /* read specific pet by id */
    @GetMapping("/{id}")
    public Pet readById(@RequestHeader("userId") Integer userID, @PathVariable(value = "id") String iden) {
        log.info("User with ID:" + userID + " has just accessed readById function");
        return petService.readById(iden);
    }

    /* read specific pet by name and age */
    @GetMapping("/{name}/{age}")
    public List<Pet> readByIdAndName(@RequestHeader("userId") Integer userID, @PathVariable(value = "name") String nm, @PathVariable(value = "age") String age) {
        log.info("User with ID:" + userID + " has just accessed readByIdAndName function");
        return petService.readByNameAndAge(nm, age);
    }

    /* count how many pets in the pet table */
    @GetMapping("/count")
    public int countPet(@RequestHeader("userId") Integer userID) throws Exception {
        log.info("User with ID:" + userID + " has just accessed count pet function");
        return petService.countPet();
    }

    /* update pet */
    @PutMapping("/{id}")
    public int updatePet(@RequestHeader("userId") Integer userID, @PathVariable(value = "id") String iden, @RequestBody Pet tempPet) throws SQLException {
        log.debug("User with ID:" + userID + " has just accessed updatePet function");
        return petService.updatePet(iden, tempPet);
    }

    /* delete pet */
    @DeleteMapping("/{id}")
    public int deletePet(@RequestHeader("userId") Integer userID, @PathVariable(value = "id") String iden) throws SQLException {
        log.warn("User with ID:" + userID + " has just accessed deletePet function");
        return petService.deletePet(iden);
    }
}
