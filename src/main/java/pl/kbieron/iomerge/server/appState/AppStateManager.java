package pl.kbieron.iomerge.server.appState;

import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;


@Component
public class AppStateManager extends Observable {

	private StateType stateType;

	private double enterPosition;

	public void enterRemoteScreen(double enterPosition) {
		this.enterPosition = enterPosition;
		stateType = StateType.ON_REMOTE;
		setChanged();
		notifyObservers();
	}

	public void exitRemote() {
		stateType = StateType.CONNECTED;
		setChanged();
		notifyObservers();
	}

	public StateType getStateType() {
		return stateType;
	}

	public double getEnterPosition() {
		return enterPosition;
	}

	public void addObservers(Observer... observers) {
		for ( Observer observer : observers ) {
			addObserver(observer);
		}
	}

	public void connected() {
		stateType = StateType.CONNECTED;
		setChanged();
		notifyObservers();
	}

	public void disconnected() {
		stateType = StateType.DISCONNECTED;
		setChanged();
		notifyObservers();
	}
}
