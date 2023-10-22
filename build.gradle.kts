plugins {
	java
	id("org.springframework.boot") version "2.7.16-SNAPSHOT"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "com.project"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.3.1")
	implementation("org.springframework.security:spring-security-core")
	implementation("org.xerial:sqlite-jdbc")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	implementation("commons-io:commons-io:2.8.0")
	implementation("org.yaml:snakeyaml")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:2.3.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
