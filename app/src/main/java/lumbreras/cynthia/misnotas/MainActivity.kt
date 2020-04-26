package lumbreras.cynthia.misnotas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    var notas = ArrayList<Nota>()
    lateinit var adaptador : AdaptadorNotas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        leerNotas()

        botonAgregar.setOnClickListener{
            var intent = Intent(this, AgregarNotaActivity::class.java)
            startActivityForResult(intent, 123)
        }

        adaptador = AdaptadorNotas(this, notas)
        listView.adapter = adaptador

    }

    fun leerNotas(){
        notas.clear()
        var carpeta = File(ubicacion().absolutePath)
        if(carpeta.exists()){
            var archivos = carpeta.listFiles()
            if(archivos!=null){
                for(archivo in archivos){
                    leerArchivo(archivo)
                }
            }
        }
    }

    //leer nota en memoria
    fun leerArchivo(archivo: File){
        val fis= FileInputStream(archivo)
        val dis= DataInputStream(fis)
        val br= BufferedReader(InputStreamReader(dis))
        var strLine: String? = br.readLine()
        var myData=""

        //se lee archivos almacenados en memoria
        while(strLine!=null){
            myData = myData+strLine
            strLine= br.readLine()
        }

        br.close()
        dis.close()
        fis.close()
        //Se elimina el .txt del nombre
        var nombre = archivo.name.substring(0, archivo.name.length-4)
        //Crea la nota y la agrega a la lista
        var nota = Nota(nombre, myData)
        notas.add(nota)
    }

    private fun ubicacion(): File{
        val folder = File(Environment.getExternalStorageDirectory(), "notas")
        if(!folder.exists()){
            folder.mkdir()
        }
        return folder
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //se activa cuando regresa del AgregarNotaActivity
        if(requestCode==123){
            leerNotas()
            adaptador.notifyDataSetChanged()
        }
    }
}
