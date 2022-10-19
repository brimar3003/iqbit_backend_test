package com.example.demo;

import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.*;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController {

    // Retorna los datos del usuario con el id enviado del JSON adjunto. En caso de que no exista retorna un error 400
    @GetMapping("/user/{id}")
    public ResponseEntity getUser(@PathVariable String id) {
        try{
            JSONObject object = getData();
            JSONObject user = new JSONObject(object.get(id).toString());
            
            return new ResponseEntity<Object>(user.toMap(), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<Object>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Retornar todos los usuarios que cumplan con la posición seleccionada en un arreglo. En caso de que no existan datos, retorna un arreglo vacío.
    @GetMapping("/users")
    public ResponseEntity getUsersByPosition(@RequestParam("position") String position) {
        List<Object> list = new ArrayList();
        try{
            
            JSONObject object = getData();
            Iterator<?> keys = object.keys();

            while(keys.hasNext()){
                String key = (String)keys.next();
                
                JSONObject item = new JSONObject(object.get(key).toString());
                if(item.get("position").equals(position)){
                    list.add(item.toMap());
                }
            }
            return new ResponseEntity<Object>(list, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<Object>(list, HttpStatus.NOT_FOUND);
        }

        
    }

    // Obtiene el json y lo mapea
    public JSONObject getData() throws IOException {
        File file = new ClassPathResource("users.json").getFile();
        String json = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

        return new JSONObject(json);
    }

}