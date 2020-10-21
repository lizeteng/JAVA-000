import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;

/**
 * @author lizeteng
 * @date 2020/10/21
 */
public class HelloClassLoader extends ClassLoader {

  private static final String PATH = "Hello.xlass";

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    try {
      byte[] bytes = getBytes();
      transform(bytes);
      return defineClass(name, bytes, 0, bytes.length);
    } catch (IOException e) {
      throw new ClassNotFoundException(String.format("[%s] not found", PATH));
    }
  }

  private byte[] getBytes() throws IOException {
    return Files.readAllBytes(new File(PATH).toPath());
  }

  private void transform(byte[] bytes) {
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (255 - bytes[i]);
    }
  }

  public static void main(String[] args) {
    try {
      Class<?> clazz = new HelloClassLoader().findClass("Hello");
      Method method = clazz.getMethod("hello");
      method.invoke(clazz.newInstance());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
