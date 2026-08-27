package pe.edu.vallegrande.app.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SparkUniversidadService {

    private final SparkSession spark;

    private final String sqlServerUrl;
    private final String sqlServerUsername;
    private final String sqlServerPassword;
    private final String sqlServerDriver;

    private final String postgresUrl;
    private final String postgresUsername;
    private final String postgresPassword;
    private final String postgresDriver;

    public SparkUniversidadService(
            @Value("${app.sqlserver.url}") String sqlServerUrl,
            @Value("${app.sqlserver.username}") String sqlServerUsername,
            @Value("${app.sqlserver.password}") String sqlServerPassword,
            @Value("${app.sqlserver.driver}") String sqlServerDriver,

            @Value("${app.postgres.url}") String postgresUrl,
            @Value("${app.postgres.username}") String postgresUsername,
            @Value("${app.postgres.password}") String postgresPassword,
            @Value("${app.postgres.driver}") String postgresDriver
    ) {

        this.sqlServerUrl = sqlServerUrl;
        this.sqlServerUsername = sqlServerUsername;
        this.sqlServerPassword = sqlServerPassword;
        this.sqlServerDriver = sqlServerDriver;

        this.postgresUrl = postgresUrl;
        this.postgresUsername = postgresUsername;
        this.postgresPassword = postgresPassword;
        this.postgresDriver = postgresDriver;

        this.spark = SparkSession.builder()
                .appName("UniversidadSparkDemo")
                .master("local[*]")
                .getOrCreate();
    }

    // Método para obtener el resumen de estudiantes y sus matriculas
    public Dataset<Row> obtenerResumen() {

        Dataset<Row> estudiantes = leerEstudiantes();

        Dataset<Row> matriculas = leerMatriculas();

        Dataset<Row> resultado = estudiantes
                .join(
                        matriculas,
                        estudiantes.col("id")
                                .equalTo(matriculas.col("estudiante_id"))
                )
                .select(
                        estudiantes.col("codigo"),
                        estudiantes.col("nombre"),
                        estudiantes.col("apellido"),
                        estudiantes.col("carrera"),
                        matriculas.col("curso"),
                        matriculas.col("nota")
                );
        return resultado;
    }

    //Conexión a SQL Server para leer la tabla de estudiantes
    private Dataset<Row> leerEstudiantes() {
        return spark.read()
                .format("jdbc")
                .option("url", sqlServerUrl)
                .option("dbtable", "dbo.estudiantes")
                .option("user", sqlServerUsername)
                .option("password", sqlServerPassword)
                .option("driver", this.sqlServerDriver)
                .load();
    }

    //Conexión a PostgreSQL para leer la tabla de matriculas
    private Dataset<Row> leerMatriculas() {
        return spark.read()
                .format("jdbc")
                .option("url", postgresUrl)
                .option("dbtable", "public.matriculas")
                .option("user", postgresUsername)
                .option("password", postgresPassword)
                .option("driver", this.postgresDriver)
                .load();
    }

}