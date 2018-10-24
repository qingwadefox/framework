package com.framework.common.utils;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class JNAUtil {

	public static <T extends Structure> T pointerToStructure(Class<T> structureClass, Pointer pointer, Integer length) throws InstantiationException, IllegalAccessException {
		T structure = structureClass.newInstance();
		// structureClass.
		// structure.getPointer();
		structure.write();
		structure.getPointer().write(0, pointer.getByteArray(0, length), 0, length);
		structure.read();
		return structure;
	}

	// public static void free(Pointer pointer) {
	// Native.free(Pointer.nativeValue(pointer));
	// }
	//
	// public static void free(Structure structure) {
	// Native.free(Pointer.nativeValue(structure.getPointer()));
	// }
	//
	// public static void free(ByReference byReference) {
	// Native.free(Pointer.nativeValue(byReference.getPointer()));
	// }

}
