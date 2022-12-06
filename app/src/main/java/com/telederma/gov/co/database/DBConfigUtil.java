package com.telederma.gov.co.database;

import com.j256.ormlite.cipher.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

import static com.j256.ormlite.cipher.android.apptools.OrmLiteConfigUtil.writeConfigFile;

/**
 * Clase utilitaria para generar el archivo de mapeo de entidades para el ORM
 * <p>
 * Created by Daniel Hernández on 6/8/2018.
 */
public class DBConfigUtil extends OrmLiteConfigUtil {

    public static void main(String[] args) throws IOException, SQLException {
        writeConfigFile("ormlite_config.txt");
    }

}
