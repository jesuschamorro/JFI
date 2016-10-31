package jfi.shape;

import java.awt.Point;

/**
 *
 * @author Jesús Chamorro Martínez (jesus@decsai.ugr.es)
 */
public class _toEraseTest {
    
    
    public _toEraseTest(){
        
    }    
        
    /***************************************************************************
       * Pruebas (usando código de Soto, que a su vez viene de un código que yo
       * tenía en C (el que usé para los cromosomas), que a su vez creo que
       * viene de un código que en su día me pasó Antonio.
       * 
       * Está todo muy caótico, es solo para testeo
    */
    
    
    
    
     private float a,b,c;
    private float px1[],py1[],pxk[],pyk[];
    
     public float[] curvatura(Point[] puntos, int np, int win, int dist){
   Point[] punAux;
   int i, k;
   //float a, b, c;
   float
   /*px1, py1,*/ mpxk, mpyk, mod1, mod2;
   //float pxk, pyk;
   px1 = new float[1];
   py1 = new float[1];
   pxk = new float[1];
   pyk = new float[1];

   float v1x, v1y, v2x, v2y;
   float v1xold = 0.0F, v1yold = 0.0F, v2xold = 0.0F, v2yold = 0.0F;
   float[] salida;
   float arc_cos, arc_cos2;
   int modulo;

   //inicializaciones
   int ptrAux = 0;

   if (win > np)
     win = np;
   salida = new float[np];
   modulo = (np < 100) ? 1 : np / 100;

   punAux = new Point[np];
   for(int p=0;p<np;p++)
     punAux[p]=new Point(puntos[p]);

   for (i = 0; i < np; i++) {
     RectaRegresion(puntos, (np + i - (win / 2 + dist) + 1) % np, win, np);
     Proyeccion(punAux[ptrAux].x, punAux[ptrAux].y, a, b, c, px1, py1);
     mpxk = mpyk = 0.0F;
     for (k = 0; k < win; k++) {
       Proyeccion(puntos[ (np + i - k) % np].x, puntos[ (np + i - k) % np].y,
                  a, b, c, pxk, pyk);
       mpxk += pxk[0];
       mpyk += pyk[0];
     }
     mpxk = mpxk / win;
     mpyk = mpyk / win;
     v1x = mpxk - px1[0];
     v1y = mpyk - py1[0];
     mod1 = (float) Math.sqrt( (double) (v1x * v1x + v1y * v1y));
     v1x /= mod1;
     v1y /= mod1;

     /* Obtenemos el vector de la segunda recta */

     RectaRegresion(puntos, (i + np + (dist - win / 2)) % np, win, np);
     Proyeccion(punAux[ptrAux].x, punAux[ptrAux].y, a, b, c, px1, py1);
     mpxk = mpyk = 0.0F;

     for (k = 0; k < win; k++) {
       Proyeccion(puntos[ (i + k) % np].x, puntos[ (i + k) % np].y, a, b, c,
                  pxk, pyk);
       mpxk += pxk[0];
       mpyk += pyk[0];
     }
     mpxk = mpxk / win;
     mpyk = mpyk / win;
     v2x = mpxk - px1[0];
     v2y = mpyk - py1[0];
     mod2 = (float) Math.sqrt( (double) (v2x * v2x + v2y * v2y));
     v2x /= mod2;
     v2y /= mod2;

     /* Calculamos  el angulo */
     //aux_cos = -(v1x*v2x+v2y*v1y);
     //salida[i]=(float)acos((double)(-(v1x*v2x+v2y*v1y)));

     /* Calculamos el signo */

     arc_cos = (float) ( (v1y < 0) ? -Math.acos( (double) v1x) :
                        Math.acos( (double) v1x));
     arc_cos2 = (float) ( (v2y < 0) ? -Math.acos( (double) v2x) :
                         Math.acos( (double) v2x));
     salida[i] = arc_cos - arc_cos2;

     if (arc_cos2 > arc_cos)
       salida[i] += (float) (Math.PI);
     else
       salida[i] -= (float) Math.PI;

     ptrAux++;
   }

   return salida;
 }
    
    
     private void RectaRegresion(Point[] elp, int inicio, int m, int n/*,float *a, float *b, float *c*/){
   Point[] X;
   float Mx, My, Sxy, Sxx, Syy;
   float aux, aux1;
   int j;
   float trS2, det;
   float lambda, delta;
   int ptrX = 0;
   int tamElp = elp.length;
   //inicializamos X
   X = new Point[tamElp];
   inicializaX(X, elp, tamElp, inicio); //X = elp+inicio;
   Mx = My = (float) 0.0;
   for (j = inicio; j < inicio + m; j++) {
     Mx += X[ptrX].x;
     My += X[ptrX].y;
     ptrX++;
     if (j == n - 1){
       X = new Point[tamElp];
       inicializaX(X, elp, tamElp, 0); //X = elp;
     }
   }
   Mx /= m;
   My /= m;
   X = new Point[tamElp];
   inicializaX(X, elp, tamElp, inicio); //X = elp+inicio;
   ptrX = 0;
   Sxx = Syy = Sxy = (float) 0.0;
   for (j = inicio; j < inicio + m; j++) {
     aux = X[ptrX].x - Mx;
     aux1 = X[ptrX].y - My;
     Sxy += aux * aux1;
     Sxx += aux * aux;
     Syy += aux1 * aux1;
     ptrX++;
     if (j == n - 1){
       X = new Point[tamElp];
       inicializaX(X, elp, tamElp, 0); //X = elp;
     }
   }
   trS2 = (Sxx + Syy) / 2;
   det = Sxx * Syy - Sxy * Sxy;
   lambda = (trS2 * trS2) - det;
   lambda = trS2 - (float) Math.sqrt( (double) lambda);
   delta = (Sxy * Sxy + (lambda - Syy) * (lambda - Syy));
   delta = (float) Math.sqrt( (double) delta);
   if (delta > 0.00001) {
     /* --- if(delta!=0) ---- Si Sxy==0 && (Sxx==0 || Syy==0) y m s ....*/
     a = (lambda - Syy) / delta;
     b = Sxy / delta;
   }
   else
   if (Syy < Sxx) { /*l¡nea horizontal */
     a = (float) 0.0;
     b = (float) 1.0;
     //if (unflag) printf ("Nos metemos en horizontal");
   }
   else {
     a = (float) 1.0;
     b = (float) 0.0;
     //if (unflag) printf ("Nos metemos en vertical");
   }
   c = - ( (a) * Mx + (b) * My);
   if (c < 0) {
     a = -a;
     b = -b;
     c = -c;
   }
 }
     
     
     private void inicializaX(Point[] X, Point[] elp, int tamElp, int inicio){
   for(int i=0;i<tamElp-inicio;i++)
     X[i]=new Point(elp[i+inicio]);
 }
    
     private void Proyeccion(int x, int y, float a, float b, float c,float[] px, float[] py){
   px[0] = (b * b * x - a * b * y - a * c) / (b * b + a * a);
   py[0] = - (b * c + a * b * x - a * a * y) / (b * b + a * a);
 }
   
     
  private void AjustarVector(float[] PatronAux, float[] Patron, int nep, int ne)
{

   /* Ajusta el patron PatronAux (de tamano 'nep') a tamano 'ne' y devuelve el resultdo en Patron */
   float pos, salto, anterior;
   int i, op, ins;

   if (ne != nep) {
     pos = salto = (float) nep / Math.abs(nep - ne);
     op = nep > ne ? 1 : 2; /* op=1 => reducir longitud ; op=2 => aumentar longitud */
     anterior = 0.0F;

     for (i = 1, ins = 0; i <= nep; i++){ /* Leemos y guardamos cada uno de los valores del vector de bandas */
         /* Leemos y guardamos cada uno de los valores del vector de bandas */
       if ( (float) i >= pos) { /* Si hemos llegado a un punto de ajuste...*/
         pos += salto;
         if (op == 2) {
           if (ins < ne) {
             Patron[ins++] = (PatronAux[i - 1] + anterior) / 2;
           }
           if (ins < ne) {
             Patron[ins++] = PatronAux[i - 1];
           }
         }
         /* Si op==1, no guardamos el elemento leido => reducimos longitud */
       }
       else if (ins < ne) {
         Patron[ins++] = PatronAux[i - 1];
       }
       anterior = PatronAux[i - 1];
     }
     if (ins < ne) /* Si falta un numero para completar... */
       for (i = ins; i < ne; i++) Patron[i] = anterior;

   }
   else for (i = 0; i < nep; i++) Patron[i] = PatronAux[i];
 }
    
}
