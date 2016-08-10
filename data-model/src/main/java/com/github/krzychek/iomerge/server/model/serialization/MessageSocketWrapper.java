package com.github.krzychek.iomerge.server.model.serialization;

import com.github.krzychek.iomerge.server.model.message.Message;
import org.nustaq.net.TCPObjectSocket;

import java.io.IOException;
import java.net.Socket;


public class MessageSocketWrapper {

	private final TCPObjectSocket tcpObjectSocket;

	public MessageSocketWrapper(String address, int port) throws IOException {
		this.tcpObjectSocket = new TCPObjectSocket(address, port, FSTConfigurationFactory.createConfiguration());
	}

	public MessageSocketWrapper(Socket socket) throws IOException {
		this.tcpObjectSocket = new TCPObjectSocket(socket, FSTConfigurationFactory.createConfiguration());
	}

	public boolean isClosed() {
		return tcpObjectSocket.isClosed();
	}

	public Message readMessage() throws IOException, ClassNotFoundException {
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
