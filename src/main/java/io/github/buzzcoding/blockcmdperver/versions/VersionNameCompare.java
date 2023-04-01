package io.github.buzzcoding.blockcmdperver.versions;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Comparator;

public class VersionNameCompare implements Comparator<String> {

	@Override
	public int compare(@NonNull String o1, @NonNull String o2) {
		int o1_int = Integer.parseInt(o1.replace(".", ""));
		int o2_int = Integer.parseInt(o2.replace(".", ""));

		return o1_int - o2_int;
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}
}