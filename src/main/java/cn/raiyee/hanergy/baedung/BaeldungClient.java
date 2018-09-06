package cn.raiyee.hanergy.baedung;

import cn.raiyee.hanergy.WsClient;

public class BaeldungClient {
    public static void main(String[] args) {
        Baeldung baeldung = WsClient.proxy(Baeldung.class, "http://127.0.0.1:8080/services/Baeldung");
        String bingoo = baeldung.hello("bingoo");
        System.out.println(bingoo);
    }
}
