package org.qingfox.framework.socket.api;

import java.io.Serializable;

public class SocketId implements Serializable {

	private static final long serialVersionUID = 7845382339781708338L;

	private String[] socketIds;

	public SocketId(String... socketIds) {
		this.socketIds = socketIds;
	}

	public static SocketId none() {
		return null;
	}

	public static SocketId create(String... socketIds) {
		return new SocketId(socketIds);
	}

	public String[] gets() {
		return this.socketIds;
	}

	public String get() {
		if (this.socketIds != null && this.socketIds.length > 0) {
			return socketIds[0];
		} else {
			return null;
		}
	}
}
