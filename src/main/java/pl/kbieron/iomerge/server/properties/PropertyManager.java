package pl.kbieron.iomerge.server.properties;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Properties;


@Component
public class PropertyManager {

	private final Log log = LogFactory.getLog(PropertyManager.class);

	@Autowired
	private ApplicationContext context;

	private Reflections reflections;

	@PostConstruct
	private void init() {
		reflections = new Reflections("pl.kbieron.iomerge.server", new FieldAnnotationsScanner());
	}

	public void readPropertiesFromFile(String fileName) {
		Properties properties = new Properties();
		try ( FileInputStream propertiesFile = new FileInputStream(fileName) ) {
			properties.load(propertiesFile);

			for ( Field field : reflections.getFieldsAnnotatedWith(ConfigProperty.class) ) {

				Object owner = context.getBean(field.getDeclaringClass());
				String serialized = properties.getProperty(getPropertyFullName(field, owner));

				if ( serialized != null && Serializable.class.isAssignableFrom(field.getType()) ) {
					Object value = deserializeFromBase64String(serialized, field.getType());
					field.setAccessible(true);
					if ( value != null ) field.set(owner, value);
				}
			}
		} catch (IOException | IllegalAccessException e) {
			log.warn(e);
		}

	}

	private String getPropertyFullName(Field field, Object owner) {
		return owner.getClass().getName() + '#' + field.getName();
	}

	private Object deserializeFromBase64String(String value, Class<?> type) throws IOException {
		if ( String.class.equals(type) ) {
			return value;
		}
		if ( Integer.class.equals(type) ) {
			return Integer.parseInt(value);
		}
		if ( Double.class.equals(type) ) {
			return Double.parseDouble(value);
		}
		if ( type.isEnum() ) {

			for ( Object o : type.getEnumConstants() ) {
				if ( o instanceof Enum && ((Enum) o).name().equalsIgnoreCase(value) ) {
					return o;
				}
			}
			return null;
		}

		InputStream base64Stream = new ByteArrayInputStream(value.getBytes());
		InputStream inputStream = new Base64InputStream(base64Stream);
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		try {
			return objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public void savePropertiesToFile(String fileName) {
		try ( FileOutputStream propertiesFile = new FileOutputStream(fileName) ) {
			Properties properties = new Properties();

			for ( Field field : reflections.getFieldsAnnotatedWith(ConfigProperty.class) ) {

				Object owner = context.getBean(field.getDeclaringClass());
				field.setAccessible(true);
				Object value = field.get(owner);
				if ( value != null ) {
					properties.setProperty(getPropertyFullName(field, owner), getSerialized(value));
				}
			}

			properties.store(propertiesFile, "IOMerge properties");
			propertiesFile.close();
		} catch (IOException | IllegalAccessException e) {
			log.error(e);
		}
	}

	private String getSerialized(Object obj) throws IOException {
		if ( obj instanceof String ) {
			return (String) obj;
		}
		if ( obj instanceof Integer || obj instanceof Double ) {
			return obj.toString();
		}
		if ( obj.getClass().isEnum() ) {
			return ((Enum) obj).name();
		}

		OutputStream byteStream = new ByteArrayOutputStream();
		OutputStream base64Stream = new Base64OutputStream(byteStream);
		ObjectOutputStream outputStream = new ObjectOutputStream(base64Stream);

		outputStream.writeObject(obj);
		return byteStream.toString();
	}

}
