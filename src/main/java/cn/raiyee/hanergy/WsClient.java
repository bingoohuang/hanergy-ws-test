package cn.raiyee.hanergy;

import lombok.val;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class WsClient {
    public static <T> T proxy(Class<T> serviceClass, String addr) {
        val f = new JaxWsProxyFactoryBean();
        f.setServiceClass(serviceClass);
        f.setAddress(addr);
        return (T) f.create();
    }
}
