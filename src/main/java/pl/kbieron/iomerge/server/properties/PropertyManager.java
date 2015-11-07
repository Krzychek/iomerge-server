package pl.kbieron.iomerge.server.properties;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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

	@Autowired
	private ApplicationContext context;

	public void readPropertiesFromFile(String fileName) throws IOException {
		Reflections reflections = new Reflections("pl.kbieron.iomerge.server");

		Properties properties = new Properties();
		try ( FileInputStream propertiesFile = new FileInputStream(fileName) ) {
			properties.load(propertiesFile);
		}

		for ( Field field : reflections.getFieldsAnnotatedWith(ConfigProperty.class) ) {

			Object owner = context.getBean(field.getDeclaringClass());
			String value = properties.getProperty(getPropertyFullName(field, owner));

			if ( Serializable.class.isAssignableFrom(field.getDeclaringClass()) ) {
				field.setAccessible(true);

				try {
					field.set(owner, deserializeFromBase64String(value));
				} catch (IllegalAccessException ignored) {}
			}
		}

	}

	private String getPropertyFullName(Field field, Object owner) {
		return owner.getClass().getName() + '#' + field.getName();
	}

	public Object deserializeFromBase64String(String string) throws IOException {
		InputStream base64Stream = new ByteArrayInputStream(string.getBytes());
		InputStream inputStream = new Base64InputStream(base64Stream);
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		try {
			return objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public void savePropertiesToFile(String fileName) throws IOException {
		Properties properties = new Properties();

		Reflections reflections = new Reflections("pl.kbieron.iomerge.server");
		for ( Field field : reflections.getFieldsAnnotatedWith(ConfigProperty.class) ) {

			Object owner = context.getBean(field.getDeclaringClass());
			field.setAccessible(true);
			try {
				Object value = field.get(owner);
				if ( value != null ) {
					properties.setProperty(getPropertyFullName(field, owner), serializeToBase64String(value));
				}
			} catch (IllegalAccessException ignored) {}
		}

		FileOutputStream propertiesFile = new FileOutputStream(fileName);
		properties.store(propertiesFile, "comment");
		propertiesFile.close();
	}

	public String serializeToBase64String(Object obj) throws IOException {
		OutputStream byteStream = new ByteArrayOutputStream();
		OutputStream base64Stream = new Base64OutputStream(byteStream);
		ObjectOutputStream outputStream = new ObjectOutputStream(base64Stream);

		outputStream.writeObject(obj);
		return byteStream.toString();
	}

}
