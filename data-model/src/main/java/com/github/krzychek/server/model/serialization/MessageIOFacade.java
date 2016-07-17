package com.github.krzychek.server.model.serialization;

import com.github.krzychek.server.model.message.Message;
import org.nustaq.net.TCPObjectSocket;

import java.io.IOException;
import java.net.Socket;


public class MessageIOFacade {

	private final TCPObjectSocket tcpObjectSocket;

	public MessageIOFacade(String address, int port) throws IOException {
		this.tcpObjectSocket = new TCPObjectSocket(address, port, FSTConfigurationFactory.createConfiguration());
	}

	public MessageIOFacade(Socket socket) throws IOException {
		this.tcpObjectSocket = new TCPObjectSocket(socket, FSTConfigurationFactory.createConfiguration());
	}

	public boolean isStopped() {
		return tcpObjectSocket.isStopped();
	}

	public boolean isClosed() {
		return tcpObjectSocket.isClosed();
	}

	public Message getMessage() throws IOException, ClassNotFoundException {
		try {
			return (Message) tcpObjectSocket.readObject();
		} catch (ClassNotFoundException | IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public void sendMessage(Message message) throws IOException {
		try {
			tcpObjectSocket.writeObject(message);
			tcpObjectSocket.flush();
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public void close() throws IOException {
		tcpObjectSocket.close(); // FIXME
	}

}
