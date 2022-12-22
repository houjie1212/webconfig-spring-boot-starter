package pers.lurker.webconfig.util;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Transmittable类型的ThreadLocal工具类，仅用于子线程需要读取父线程ThreadLocal数据的场景，谨慎使用！
 * @version V1.0
 * @date 2018年12月11日 下午5:14:20
 */
public class ThreadLocalTransmittableUtil {

  /**
   * ThreadLocal对象
   */
  private static ThreadLocal<ConcurrentHashMap<String, Object>> dataTl =
      new TransmittableThreadLocal<>();

  @SuppressWarnings("unchecked")
  public static <T> T get(String key, Class<T> clazz) {
    ConcurrentHashMap<String, Object> datas = dataTl.get();
    Object obj = (datas != null) ? datas.get(key) : null;
    if (obj == null) {
      return null;
    }
    return (T) obj;
  }

  public static void set(String key, Object value) {
    ConcurrentHashMap<String, Object> datas = dataTl.get();
    if (datas == null) {
      datas = new ConcurrentHashMap<>();
      dataTl.set(datas);
    }
    datas.put(key, value);
  }

  public static void remove(String key) {
    ConcurrentHashMap<String, Object> datas = dataTl.get();
    if (datas != null) {
      datas.remove(key);
    }
  }
  
  public static void remove() {
    dataTl.remove();
  }
}
