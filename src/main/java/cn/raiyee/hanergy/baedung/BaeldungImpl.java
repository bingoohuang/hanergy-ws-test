package cn.raiyee.hanergy.baedung;

import javax.jws.WebService;

@WebService(endpointInterface = "cn.raiyee.hanergy.baedung.Baeldung")
public class BaeldungImpl implements Baeldung {
    private int counter;

    public String hello(String name) {
        return "Hello " + name + "!";
    }

    public String register(Student student) {
        counter++;
        return student.getName() + " is registered student number " + counter;
    }
}