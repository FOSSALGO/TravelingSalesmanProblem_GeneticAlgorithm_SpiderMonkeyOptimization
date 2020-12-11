package fosalgo;

public class Data {

    private Vertex[] arrayVertex = null;

    public Data(Vertex[] arrayVertex) {
        this.arrayVertex = arrayVertex;
    }

    public Vertex[] getArrayVertex() {
        return arrayVertex;
    }

    public void setArrayVertex(Vertex[] arrayVertex) {
        this.arrayVertex = arrayVertex;
    }
    
    

    public double hitungJarak(int indexVertex1, int indexVertex2) {
        double jarak = -1;
        //validasi
        if (arrayVertex != null
                && indexVertex1 >= 0
                && indexVertex1 < arrayVertex.length
                && indexVertex2 >= 0
                && indexVertex2 < arrayVertex.length) {
            //jika valid
            Vertex v1 = arrayVertex[indexVertex1];
            Vertex v2 = arrayVertex[indexVertex2];

            //hitung jarak euclid
            double x1 = v1.x;
            double y1 = v1.y;

            double x2 = v2.x;
            double y2 = v2.y;

            double x = x1 - x2;//selisih x
            double y = y1 - y2;//selisih y

            double xx = x * x;//kuadrat x
            double yy = y * y;//kuadrat y

            double xxyy = xx + yy;
            double z = Math.sqrt(xxyy);
            jarak = z;
        }
        return jarak;
    }

    @Override
    public String toString() {
        String result = null;
        if (arrayVertex != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("Data----------------------------------------------\n");
            sb.append("label ( x , y )\n");
            for (int i = 0; i < arrayVertex.length; i++) {
                sb.append(arrayVertex[i].toString() + "\n");
            }
            sb.append("--------------------------------------------------\n");
            result = sb.toString();
        }
        return result;
    }

}
