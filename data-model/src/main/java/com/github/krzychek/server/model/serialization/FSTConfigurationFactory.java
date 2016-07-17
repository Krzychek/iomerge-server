package com.github.krzychek.server.model.serialization;

import com.github.krzychek.server.model.message.keyboard.BackBtnClick;
import com.github.krzychek.server.model.message.keyboard.HomeBtnClick;
import com.github.krzychek.server.model.message.keyboard.KeyClick;
import com.github.krzychek.server.model.message.keyboard.KeyPress;
import com.github.krzychek.server.model.message.keyboard.KeyRelease;
import com.github.krzychek.server.model.message.keyboard.MenuBtnClick;
import com.github.krzychek.server.model.message.misc.ClipboardSync;
import com.github.krzychek.server.model.message.misc.EdgeSync;
import com.github.krzychek.server.model.message.misc.Heartbeat;
import com.github.krzychek.server.model.message.misc.RemoteExit;
import com.github.krzychek.server.model.message.mouse.MouseMove;
import com.github.krzychek.server.model.message.mouse.MousePress;
import com.github.krzychek.server.model.message.mouse.MouseRelease;
import com.github.krzychek.server.model.message.mouse.MouseWheel;
import org.nustaq.serialization.FSTConfiguration;


/**
 * configures FST library
 *
 * @see FSTConfigurationFactory#createConfiguration
 */
class FSTConfigurationFactory {


	/**
	 * configures FST library
	 */
	static synchronized FSTConfiguration createConfiguration() {
		FSTConfiguration conf = FSTConfiguration.createAndroidDefaultConfiguration();

		conf.registerClass(
				BackBtnClick.class, HomeBtnClick.class, MenuBtnClick.class, KeyClick.class, KeyPress.class, KeyRelease.class,
				ClipboardSync.class, EdgeSync.class, Heartbeat.class, RemoteExit.class,
				MousePress.class, MouseRelease.class, MouseMove.class, MouseWheel.class
		);

		conf.setPreferSpeed(true); // TODO benchmark true against false
		conf.setShareReferences(false);

		return conf;
	}
}
