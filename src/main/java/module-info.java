module javafx {
	exports org.juju.work;

	requires javafx.base;
	requires transitive javafx.controls;
	requires javafx.graphics;
	requires spring.beans;
	requires spring.boot.autoconfigure;
}