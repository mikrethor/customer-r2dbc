package com.digitowl.r2dbc.config

import com.digitowl.r2dbc.repository.CustomerRepository
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.core.DatabaseClient

import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import ru.yandex.qatools.embed.postgresql.PostgresProcess
import ru.yandex.qatools.embed.postgresql.PostgresStarter
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig
import ru.yandex.qatools.embed.postgresql.distribution.Version
import java.io.IOException
import java.sql.DriverManager
import java.util.*


@Configuration
@EnableR2dbcRepositories(basePackageClasses = [CustomerRepository::class])
class DbConfig : AbstractR2dbcConfiguration() {

    @Value("\${data.postgres.host:localhost}")
    internal var host: String? = null
    @Value("\${spring.data.postgres.port:5432}")
    internal var port: Int? = null
    @Value("\${spring.data.postgres.database:test}")
    internal var database: String? = null
    @Value("\${spring.data.postgres.username:user}")
    internal var username: String? = null
    @Value("\${spring.data.postgres.password:pass}")
    internal var password: String? = null


    private val DEFAULT_ADDITIONAL_INIT_DB_PARAMS = Arrays
            .asList("--nosync", "--locale=en_US.UTF-8")

    @Bean
    @Throws(IOException::class)
    fun postgresConfig(): PostgresConfig {

        val postgresConfig = PostgresConfig(Version.V11_1,
                AbstractPostgresConfig.Net(host, port!!),
                AbstractPostgresConfig.Storage(database),
                AbstractPostgresConfig.Timeout(),
                AbstractPostgresConfig.Credentials(username, password)
        )

        postgresConfig.additionalInitDbParams.addAll(DEFAULT_ADDITIONAL_INIT_DB_PARAMS)

        return postgresConfig
    }

    @Bean(destroyMethod = "stop")
    @Throws(IOException::class)
    fun postgresProcess(config: PostgresConfig): PostgresProcess {
        val runtime = PostgresStarter.getDefaultInstance()
        val exec = runtime.prepare(config)
        val postgres = exec.start()
        // connecting to a running Postgres and feeding up the database
        val conn = DriverManager.getConnection("jdbc:postgresql://$host:$port/$database", username, password)
        conn.createStatement().execute("CREATE TABLE customer ( id SERIAL PRIMARY KEY, firstname VARCHAR(100) NOT NULL, lastname VARCHAR(100) NOT NULL);")
        return postgres
    }


    @Bean
    internal fun databaseClient(connectionFactory: ConnectionFactory): DatabaseClient {
        return DatabaseClient.create(connectionFactory)
    }

    @DependsOn("postgresProcess")
    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(host!!)
                        .port(port!!)
                        .database(database)
                        .username(username!!)
                        .password(password!!).build()
        )
    }
}
