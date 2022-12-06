package com.telederma.gov.co;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;

import com.telederma.gov.co.patologia.AnatomyItem;
import com.telederma.gov.co.patologia.BitmapUtils;
import com.telederma.gov.co.patologia.LocationZone;

import java.util.ArrayList;
import java.util.List;

public abstract class Body {
    private  static List<LocationZone> map_body_parts_woman      = new ArrayList<>();
    private  static List<LocationZone> map_body_back_parts_woman = new ArrayList<>();

    private  static List<LocationZone> map_body_parts_men      = new ArrayList<>();
    private  static List<LocationZone> map_body_back_parts_men = new ArrayList<>();



    private static List<LocationZone> list_foot_left_body_parts_woman = new ArrayList<>();
    private static List<LocationZone> list_foot_right_parts_woman = new ArrayList<>();
    private static List<LocationZone> list_foot_plant_right_parts_woman = new ArrayList<>();
    private static List<LocationZone> list_foot_plant_left_parts_woman = new ArrayList<>();
    private static List<LocationZone> list_hand_left_body_parts_woman = new ArrayList<>();
    private static List<LocationZone> list_hand_left_back_body_parts_woman = new ArrayList<>();
    private static List<LocationZone> list_hand_right_body_parts_woman = new ArrayList<>();
    private static List<LocationZone> list_hand_right_back_body_parts_woman = new ArrayList<>();
    private static List<LocationZone> list_gluteos_body_parts_woman = new ArrayList<>();


    private static List<LocationZone> list_torso_right_body_parts_woman = new ArrayList<>();
    private static List<LocationZone> list_private_body_parts_woman = new ArrayList<>();
    private static List<LocationZone> list_face_body_parts_woman = new ArrayList<>();
    private static List<LocationZone> list_face_back_body_parts_woman = new ArrayList<>();

    private static List<LocationZone> list_torso_right_body_parts_men = new ArrayList<>();
    private static List<LocationZone> list_private_body_parts_men = new ArrayList<>();
    private static List<LocationZone> list_face_body_parts_men = new ArrayList<>();
    private static List<LocationZone> list_face_back_body_parts_men = new ArrayList<>();


    public static final int FRENTE	=	1;
    public static final int REGION_FRONTAL_CUERO_CABELLUDO	=	2;
    public static final int REGION_FRONTOTEMPORAL_DERECHA	=	3;
    public static final int VERTEX	=	4;
    public static final int REGION_TEMPORAL_IZQUIERDA	=	5;
    public static final int REGION_PREAURICULAR_DERECHO	=	6;
    public static final int PABELLON_AURICULAR_DERECHO	=	7;
    public static final int REGION_SUPRACILIAR_IZQUIERDA	=	8;
    public static final int CEJA_DERECHA	=	9;
    public static final int PARPADO_SUPERIOR_DERECHO	=	10;
    public static final int PARPADO_INFERIOR_DERECHO	=	11;
    public static final int VERTIENTE_NASAL_DERECHO	=	12;
    public static final int DORSO_NASAL	=	13;
    public static final int ALA_NASAL_DERECHA	=	14;
    public static final int PUNTA_NASAL	=	15;
    public static final int MEJILLA_DERECHA	=	16;
    public static final int REGION_MAXILAR_DERECHO	=	17;
    public static final int LABIO_SUPERIOR	=	18;
    public static final int LABIO_INFERIOR	=	19;
    public static final int MENTON	=	20;
    public static final int CUELLO	=	21;
    public static final int REGION_SUPRACLAVICULAR	=	22;
    public static final int HOMBRO_IZQUIERDO	=	23;
    public static final int REGION_PREESTERNAL	=	24;
    public static final int TORAX_ANTERIOR	=	25;
    public static final int MUNECA_POSTERIOR_IZQUIERDA	=	26;
    public static final int MUNECA_POSTERIOR_DERECHA	=	27;
    public static final int AXILA_IZQUIERDA	=	28;
    public static final int ABDOMEN	=	29;
    public static final int ANTEBRAZO_IZQUIERDO	=	30;
    public static final int MUNECA_IZQUIERDA	=	31;
    public static final int PALMA_POSTERIOR	=	32;
    public static final int HOMBRO_POSTERIOR_IZQUIERDO	=	33;
    public static final int PENE	=	34;
    public static final int ESCROTO	=	35;
    public static final int HOMBRO_POSTERIOR_DERECHO	=	36;
    public static final int CODO_POSTERIOR_DERECHO	=	37;
    public static final int CARA_ANTERIOR_MUSLO	=	38;
    public static final int CODO_POSTERIOR_IZQUIERDO	=	39;
    public static final int PANTORRILLA_POSTERIOR_IZQUIERDA	=	40;
    public static final int RODILLA_IZQUIERDA	=	41;
    public static final int CANILLA_IZQUIERDA	=	42;
    public static final int MALEOLO_INTERNO_Y_EXTERNO_IZQUIERDO	=	43;
    public static final int DORSO_DE_PIE_IZQUIERDO	=	44;
    public static final int MALEOLO_INTERNO_Y_EXTERNO_DERECHO	=	45;
    public static final int DORSO_DE_PIE_DERECHO	=	46;
    public static final int REGION_OCCIPITAL	=	47;
    public static final int REGION_RETROAURICULAR_IZQUIERDA	=	48;
    public static final int NUCA	=	49;
    public static final int PANTORRILLA_POSTERIOR_DERECHA	=	50;
    public static final int ESPALDA	=	51;
    public static final int TETILLA_DERECHA	=	52;
    public static final int TETILLA_IZQUIERDA	=	53;
    public static final int GLUTEO_IZQUIERDO	=	54;
    public static final int PLIEGUE_INTERGLUTEO	=	55;
    public static final int PERINE	=	56;
    public static final int CARA_POSTERIOR_MUSLO_IZQUIERDO	=	57;
    public static final int PLIEGUE_POPLITEO_IZQUIERDO	=	58;
    public static final int TALON_IZQUIERDO	=	59;
    public static final int DORSO_DE_MANO	=	60;
    public static final int UNA	=	61;
    public static final int REGION_SUBMAMARIA_DERECHA	=	62;
    public static final int REGION_SUBMAMARIA_IZQUIERDA	=	63;
    public static final int PESTANA_DERECHA	=	64;
    public static final int FOSA_NASAL	=	65;
    public static final int PIEL_DE_LABIO_SUPERIOR	=	66;
    public static final int PEZON_IZQUIERDO	=	67;
    public static final int AREOLA_IZQUIERDA	=	68;
    public static final int PUBIS2	=	69;
    public static final int VULVA	=	70;
    public static final int PERINE2	=	71;
    public static final int REGION_FRONTOTEMPORAL_IZQUIERDO	=	72;
    public static final int MEJILLA_IZQUIERDA	=	73;
    public static final int VERTIENTE_NASAL_IZQUIERDO	=	74;
    public static final int REGION_MAXILAR_IZQUIERDO	=	75;
    public static final int ALA_NASAL_IZQUIERDA	=	76;
    public static final int PABELLON_AURICULAR_IZQUIERDO	=	77;
    public static final int REGION_PREAURICULAR_IZQUIERDA	=	78;
    public static final int PARPADO_INFERIOR_IZQUIERDO	=	79;
    public static final int PARPADO_SUPERIOR_IZQUIERDO	=	80;
    public static final int CEJA_IZQUIERDA	=	81;
    public static final int PESTANA_IZQUIERDA	=	82;
    public static final int AXILA_DERECHO	=	83;
    public static final int PEZON_DERECHO	=	84;
    public static final int AREOLA_DERECHA	=	85;
    public static final int COSTILLAS_DERECHA	=	86;
    public static final int COSTILLAS_IZQUIERDAS	=	87;
    public static final int PALMA_DERECHA	=	88;
    public static final int MENIQUE_DERECHO_PALMA	=	89;
    public static final int MENIQUE_DERECHO_POSTERIOR	=	90;
    public static final int ANULAR_DERECHO_PALMA	=	91;
    public static final int ANULAR_DERECHO_POSTERIOR	=	92;
    public static final int MEDIO_DERECHO_PALMA	=	93;
    public static final int MEDIO_DERECHO_POSTERIOR	=	94;
    public static final int INDICE_DERECHO_PALMA	=	95;
    public static final int INDICE_DERECHO_POSTERIOR	=	96;
    public static final int PULGAR_DERECHO_PALMA	=	97;
    public static final int PULGAR_DERECHO_POSTERIOR	=	98;
    public static final int UNA_MENIQUE_DERECHO	=	99;
    public static final int UNA_ANULAR_DERECHO	=	100;
    public static final int UNA_MEDIO_DERECHO	=	101;
    public static final int UNA_INDICE_DERECHO	=	102;
    public static final int UNA_PULGAR_DERECHO	=	103;
    public static final int PALMA_IZQUIERDA	=	104;
    public static final int MENIQUE_IZQUIERDO_PALMA	=	105;
    public static final int MENIQUE_IZQUIERDO_POSTERIOR	=	106;
    public static final int ANULAR_IZQUIERDO_PALMA	=	107;
    public static final int ANULAR_IZQUIERDO_POSTERIOR	=	108;
    public static final int MEDIO_IZQUIERDO_PALMA	=	109;
    public static final int MEDIO_IZQUIERDO_POSTERIOR	=	110;
    public static final int INDICE_IZQUIERDO_PALMA	=	111;
    public static final int INDICE_IZQUIERDO_POSTERIOR	=	112;
    public static final int PULGAR_IZQUIERDO_PALMA	=	113;
    public static final int PULGAR_DERECHO_POSTERIOR_MANO	=	114;
    public static final int PALMA_IZQUIERDA_POSTERIOR	=	115;
    public static final int UNA_MENIQUE_IZQUIERDO	=	116;
    public static final int UNA_ANULAR_IZQUIERDO	=	117;
    public static final int UNA_MEDIO_IZQUIERDO	=	118;
    public static final int UNA_INDICE_IZQUIERDO	=	119;
    public static final int UNA_PULGAR_IZQUIERDO	=	120;
    public static final int PLANTA_IZQUIERDA	=	121;
    public static final int MENIQUE_IZQUIERDO_PLANTA	=	122;
    public static final int MENIQUE_IZQUIERDO_POSTERIOR2	=	123;
    public static final int ANULAR_IZQUIERDO_PLANTA	=	124;
    public static final int ANULAR_IZQUIERDO_POSTERIOR4	=	125;
    public static final int MEDIO_IZQUIERDO_PLANTA	=	126;
    public static final int MEDIO_IZQUIERDO_POSTERIOR2	=	127;
    public static final int INDICE_IZQUIERDO_PLANTA	=	128;
    public static final int INDICE_IZQUIERDO_POSTERIOR2	=	129;
    public static final int PULGAR_IZQUIERDO_PLANTA	=	130;
    public static final int PULGAR_DERECHO_POSTERIOR22	=	131;
    public static final int UNA_MENIQUE_IZQUIERDO_PIE	=	132;
    public static final int UNA_ANULAR_IZQUIERDO_PIE	=	133;
    public static final int UNA_MEDIO_IZQUIERDO_PIE	=	134;
    public static final int UNA_INDICE_IZQUIERDO_PIE	=	135;
    public static final int UNA_PULGAR_IZQUIERDO_PIE=	136;
    public static final int PLANTA_DERECHA	=	137;
    public static final int MENIQUE_DERECHO_PLANTA	=	138;
    public static final int MENIQUE_DERECHO_POSTERIOR_PIE	=	139;
    public static final int ANULAR_DERECHO_PLANTA	=	140;
    public static final int ANULAR_DERECHO_POSTERIOR_PIE	=	141;
    public static final int MEDIO_DERECHO_PLANTA	=	142;
    public static final int MEDIO_DERECHO_POSTERIOR_PIE	=	143;
    public static final int INDICE_DERECHO_PLANTA	=	144;
    public static final int INDICE_DERECHO_POSTERIOR_PIE	=	145;
    public static final int PULGAR_DERECHO_PLANTA	=	146;
    public static final int PULGAR_DERECHO_POSTERIOR_PIE	=	147;
    public static final int UNA_MENIQUE_DERECHO_PIE	=	148;
    public static final int UNA_ANULAR_DERECHO_PIE	=	149;
    public static final int UNA_MEDIO_DERECHO_PIE	=	150;
    public static final int UNA_INDICE_DERECHO_PIE	=	151;
    public static final int UNA_PULGAR_DERECHO_PIE	=	152;
    public static final int ANO	=	153;
    public static final int ABERTURA_URETRAL	=	154;
    public static final int CLITORIS	=	155;
    public static final int LABIO_MAYOR_DERECHO	=	156;
    public static final int LABIO_MAYOR_IZQUIERDO	=	157;
    public static final int LABIOS_MENORES	=	158;
    public static final int VAGINA	=	159;
    public static final int MUSLO_GENITAL_DERECHO	=	160;
    public static final int MUSLO_GENITAL_IZQUIERDO	=	161;
    public static final int REGION_RETROAURICULAR_DERECHA	=	162;
    public static final int GLUTEO_DERECHO	=	163;
    public static final int REGION_TEMPORAL_DERECHA	=	164;
    public static final int REGION_SUPRACILIAR_DERECHA	=	165;
    public static final int HOMBRO_DERECHO	=	166;
    public static final int ANTEBRAZO_DERECHO	=	167;
    public static final int MUNECA_DERECHO	=	168;
    public static final int CARA_POSTERIOR_MUSLO_DERECHO	=	169;
    public static final int CARA_ANTERIOR_MUSLO_DERECHO	=	170;
    public static final int RODILLA_DERECHO	=	171;
    public static final int CANILLA_DERECHA	=	172;
    public static final int TALON_DERECHO	=	173;
    public static final int PANTORRILLA_IZQUIERDA	=	174;
    public static final int PANTORRILLA_DERECHA	=	175;
    public static final int PLIEGUE_POPLITEO_DERECHO	=	176;
    public static final int CODO_DERECHO	=	177;
    public static final int HOMBRO_DERECHO_ESPALDA	=	178;
    public static final int HOMBRO_IZQUIERDO_ESPALDA	=	179;
    public static final int ANTEBRAZO_DERECHO_ESPALDA	=	180;
    public static final int ANTEBRAZO_IZQUIERDO_ESPALDA	=	181;
    public static final int CUERPO	=	182;




    public static final int CABEZA	                =	200;
    public static final int TORZO         	        =	201;
    public static final int ZONA_GENITAL   	        =	202;
    public static final int MANO_DERECHA	        =	203;
    public static final int MANO_IZQUIERDA	        =	204;
    public static final int PIE_DERECHO	            =	205;
    public static final int PIE_IZQUIERDO	        =	206;
    public static final int CABEZA_POSTERIOR        =	207;
    public static final int POSTERIOR_GLUTEO        =	208;
    public static final int MANO_POSTERIOR_DERECHA	=	209;
    public static final int MANO_POSTERIOR_IZQUIERDA=	210;





    public static List<AnatomyItem> getPart(int genre , int part, Context context)
    {
         switch (part)
         {
            case 200 :
                 //CARA
                  if(genre == 1)
                      return createPoints( getFaceMen(), context);
                  return createPoints( getFaceWoman(), context);
             case 201 :
                 //torso right
                 if(genre == 1)
                     return createPoints( getTorsoMen(), context);
                 return createPoints( getTorsoWoman(), context);
             case 202 :
                 //private parts
                 if(genre == 1)
                     return createPoints( getPrivatePartMen(), context);
                 return createPoints( getPrivateWoman(), context);
             case 203 :
                     return createPoints( getHandLeft(), context);
             case 204 :
                     return createPoints(getHandRight(), context);
             case 205 :
                     return createPoints( getFootRight(), context);
             case 206 :
                    return createPoints( getFootLeft(), context);
             case 207 :
                 //face_back
                 if(genre == 1)
                      return createPoints( getFaceMenBack(), context);
                 else
                     return createPoints( getFaceBackWoman(), context);
             case 208 :
                 //gluteos
                     return createPoints(getGluteos(), context);
             case 209 :
                 //hand left back
                     return createPoints( getHandLeftBack(), context);
             case 210 :
                 //hand right back
                     return createPoints( getHandRigthBack(), context);
         }
         return null;
    }




    public static List<AnatomyItem>  getBody(int genre ,boolean cara, Context context)
    {
        if (cara) {
            if (genre == 1)
                    return createPoints(getBodyMen(), context);
            return createPoints( getBodyWoman(), context);
         }
         else {

                if(genre ==1)
                        return createPoints( getBackBodyMen(), context);
                    return createPoints( getBackBodyWoman(), context);
        }
    }



    private static  List<AnatomyItem> createPoints( List<LocationZone> listZone, Context context)
    {
        List<AnatomyItem> part = new ArrayList<>();
        for (LocationZone location : listZone) {
            part.add(new AnatomyItem(location.getId() + "", new PointF(location.getX(), location.getY()),
                    BitmapUtils.resAsBitmap(location.getId_iamgen_izquierdo(),
                            ContextCompat.getDrawable(context, location.getId_iamgen_izquierdo())),
                    null));
        }
        return part;
    }


    private static List<LocationZone> getTorsoWoman()
    {
        if(list_torso_right_body_parts_woman.size()<1) {
            list_torso_right_body_parts_woman.add(new LocationZone(AXILA_IZQUIERDA, 0.89f, 0.26f, R.drawable.axila_der_1, R.drawable.axila_der));
            list_torso_right_body_parts_woman.add(new LocationZone(AXILA_DERECHO, 0.11f, 0.26f, R.drawable.axila_izq_1, R.drawable.axila_izq));
            list_torso_right_body_parts_woman.add(new LocationZone(AREOLA_IZQUIERDA, 0.795f, 0.62f, R.drawable.areola_der_1, R.drawable.areola_der));
            list_torso_right_body_parts_woman.add(new LocationZone(AREOLA_DERECHA, 0.21f, 0.62f, R.drawable.areola_izq_1, R.drawable.areola_izq));
            list_torso_right_body_parts_woman.add(new LocationZone(PEZON_IZQUIERDO, 0.795f, 0.62f, R.drawable.pezon_der_1, R.drawable.pezon_der));
            list_torso_right_body_parts_woman.add(new LocationZone(PEZON_DERECHO, 0.21f, 0.62f, R.drawable.pezon_izq_1, R.drawable.pezon_izq));
            list_torso_right_body_parts_woman.add(new LocationZone(REGION_SUPRACLAVICULAR, 0.5f, 0.085f, R.drawable.supraclavicular_1, R.drawable.supraclavicular));
            list_torso_right_body_parts_woman.add(new LocationZone(REGION_PREESTERNAL, 0.5f, 0.25f, R.drawable.preesternal_1, R.drawable.preesternal));
            list_torso_right_body_parts_woman.add(new LocationZone(TORAX_ANTERIOR, 0.5f, 0.45f, R.drawable.torax_anterior_1, R.drawable.torax_anterior));
            list_torso_right_body_parts_woman.add(new LocationZone(REGION_SUBMAMARIA_IZQUIERDA, 0.78f, 0.62f, R.drawable.submamaria_der_1, R.drawable.submamaria_der));
            list_torso_right_body_parts_woman.add(new LocationZone(REGION_SUBMAMARIA_DERECHA, 0.22f, 0.62f, R.drawable.submamaria_izq_1, R.drawable.submamaria_izq));
            list_torso_right_body_parts_woman.add(new LocationZone(COSTILLAS_IZQUIERDAS, 0.81f, 0.8f, R.drawable.costilla_der_1, R.drawable.costilla_der));
            list_torso_right_body_parts_woman.add(new LocationZone(COSTILLAS_DERECHA, 0.186f, 0.8f, R.drawable.costilla_izq_1, R.drawable.costilla_izq));
        }

                return list_torso_right_body_parts_woman;
    }

    private static List<LocationZone> getPrivateWoman()
    {
        if(list_private_body_parts_woman.size()<1) {
            list_private_body_parts_woman.add(new LocationZone(ANO, 0.5f, 0.75f, R.drawable.ano_1, R.drawable.ano));
            list_private_body_parts_woman.add(new LocationZone(ABERTURA_URETRAL, 0.501f, 0.47f, R.drawable.abertura_uretral_1, R.drawable.abertura_uretral));
            list_private_body_parts_woman.add(new LocationZone(CLITORIS, 0.501f, 0.3f, R.drawable.clitoris_1, R.drawable.clitoris));
            list_private_body_parts_woman.add(new LocationZone(LABIO_MAYOR_IZQUIERDO, 0.55f, 0.47f, R.drawable.labio_mayor_derecho_1, R.drawable.labio_mayor_derecho));
            list_private_body_parts_woman.add(new LocationZone(LABIO_MAYOR_DERECHO, 0.45f, 0.47f, R.drawable.labio_mayor_izq_1, R.drawable.labio_mayor_izq));
            list_private_body_parts_woman.add(new LocationZone(LABIOS_MENORES, 0.501f, 0.5f, R.drawable.labios_menores_1, R.drawable.labios_menores));
            list_private_body_parts_woman.add(new LocationZone(VAGINA, 0.501f, 0.5f, R.drawable.vagina_1, R.drawable.vagina));
            list_private_body_parts_woman.add(new LocationZone(MUSLO_GENITAL_IZQUIERDO, 0.78f, 0.495f, R.drawable.muslo_genitales_der_1, R.drawable.muslo_genitales_der));
            list_private_body_parts_woman.add(new LocationZone(MUSLO_GENITAL_DERECHO, 0.22f, 0.495f, R.drawable.muslo_genitales_izq_1, R.drawable.muslo_genitales_izq));
        }
        return list_private_body_parts_woman;
    }




    private  static  List<LocationZone>  getBodyMen()
    {
        if(map_body_parts_men.size()<1) {
            map_body_parts_men.add(new LocationZone(CABEZA, 0.51f, 0.089f, R.drawable.hombre_cabezapequnia_, R.drawable.hombre_cabezapequnia));
            map_body_parts_men.add(new LocationZone(ABDOMEN, 0.5f, 0.454f, R.drawable.torzo_16_1, R.drawable.torzo_16));
            map_body_parts_men.add(new LocationZone(TORZO, 0.51f, 0.3495f, R.drawable.hombre_torso_, R.drawable.hombre_torso));
            map_body_parts_men.add(new LocationZone(ZONA_GENITAL, 0.51f, 0.4878f, R.drawable.hombre_genitales_, R.drawable.hombre_genitales));
            map_body_parts_men.add(new LocationZone(MANO_DERECHA, 0.8785f, 0.545f, R.drawable.hombre_mano_der_, R.drawable.hombre_mano_der));
            map_body_parts_men.add(new LocationZone(MANO_IZQUIERDA, 0.142f, 0.545f, R.drawable.hombre_mano_izq_, R.drawable.hombre_mano_izq));
            map_body_parts_men.add(new LocationZone(PIE_DERECHO, 0.585f, 0.97f, R.drawable.hombre_pie_der_, R.drawable.hombre_pie_der));
            map_body_parts_men.add(new LocationZone(PIE_IZQUIERDO, 0.425f, 0.97f, R.drawable.hombre_pie_izq_, R.drawable.hombre_pie_izq));
            map_body_parts_men.add(new LocationZone(HOMBRO_IZQUIERDO, 0.74f, 0.27f, R.drawable.hombre_hombro_der_, R.drawable.hombre_hombro_der));
            map_body_parts_men.add(new LocationZone(HOMBRO_DERECHO, 0.27f, 0.27f, R.drawable.hombre_hombro_izq_, R.drawable.hombre_hombro_izq));
            map_body_parts_men.add(new LocationZone(ANTEBRAZO_IZQUIERDO, 0.765f, 0.414f, R.drawable.hombre_antebrazo_der_, R.drawable.hombre_antebrazo_der));
            map_body_parts_men.add(new LocationZone(ANTEBRAZO_DERECHO, 0.255f, 0.41f, R.drawable.hombre_antebrazo_izq_, R.drawable.hombre_antebrazo_izq));
            map_body_parts_men.add(new LocationZone(MUNECA_IZQUIERDA, 0.815f, 0.49f, R.drawable.hombre_muneca_der_, R.drawable.hombre_muneca_der));
            map_body_parts_men.add(new LocationZone(MUNECA_DERECHO, 0.2f, 0.49f, R.drawable.hombre_muneca_izq_, R.drawable.hombre_muneca_izq));
            map_body_parts_men.add(new LocationZone(CARA_ANTERIOR_MUSLO, 0.61f, 0.58f, R.drawable.hombre_muslo_der_, R.drawable.hombre_muslo_der));
            map_body_parts_men.add(new LocationZone(CARA_ANTERIOR_MUSLO_DERECHO, 0.405f, 0.58f, R.drawable.hombre_muslo_izq_, R.drawable.hombre_muslo_izq));
            map_body_parts_men.add(new LocationZone(RODILLA_DERECHO, 0.418f, 0.738f, R.drawable.hombre_rodilla_der_, R.drawable.hombre_rodilla_der));
            map_body_parts_men.add(new LocationZone(RODILLA_IZQUIERDA, 0.59f, 0.738f, R.drawable.hombre_rodilla_izq_, R.drawable.hombre_rodilla_izq));
//            map_body_parts_men.add(new LocationZone(RODILLA_DERECHO, 0.59f, 0.738f, R.drawable.hombre_rodilla_der_, R.drawable.hombre_rodilla_der));
//            map_body_parts_men.add(new LocationZone(RODILLA_IZQUIERDA, 0.418f, 0.738f, R.drawable.hombre_rodilla_izq_, R.drawable.hombre_rodilla_izq));
            map_body_parts_men.add(new LocationZone(CANILLA_IZQUIERDA, 0.61f, 0.85f, R.drawable.hombre_canilla_der_, R.drawable.hombre_canilla_der));
            map_body_parts_men.add(new LocationZone(CANILLA_DERECHA, 0.405f, 0.85f, R.drawable.hombre_canilla_izq_, R.drawable.hombre_canilla_izq));
        }
        return map_body_parts_men;
    }

    private static List<LocationZone> getBackBodyMen()
    {
        if(map_body_back_parts_men.size()<1) {
            map_body_back_parts_men.add(new LocationZone(MUNECA_POSTERIOR_IZQUIERDA, 0.84f, 0.5f, R.drawable.hombre_posterior_munieca_der_, R.drawable.hombre_posterior_munieca_der));
            map_body_back_parts_men.add(new LocationZone(MUNECA_POSTERIOR_DERECHA, 0.15f, 0.5f, R.drawable.hombre_posterior_munieca_izq_, R.drawable.hombre_posterior_munieca_izq));
            map_body_back_parts_men.add(new LocationZone(HOMBRO_POSTERIOR_DERECHO, 0.76f, 0.28f, R.drawable.hombre_posterior_hombro_der_, R.drawable.hombre_posterior_hombro_der));
            map_body_back_parts_men.add(new LocationZone(HOMBRO_POSTERIOR_IZQUIERDO, 0.24f, 0.28f, R.drawable.hombre_posterior_hombro_izq_, R.drawable.hombre_posterior_hombro_izq));
            map_body_back_parts_men.add(new LocationZone(ANTEBRAZO_DERECHO_ESPALDA, 0.78f, 0.44f, R.drawable.hombre_posterior_antebrazo_der_, R.drawable.hombre_posterior_antebrazo_der));
            map_body_back_parts_men.add(new LocationZone(ANTEBRAZO_IZQUIERDO_ESPALDA, 0.215f, 0.44f, R.drawable.hombre_posterior_antebrazo_izq_, R.drawable.hombre_posterior_antebrazo_izq));
            map_body_back_parts_men.add(new LocationZone(CODO_POSTERIOR_DERECHO, 0.76f, 0.38f, R.drawable.hombre_posterior_codo_der_, R.drawable.hombre_posterior_codo_der));
            map_body_back_parts_men.add(new LocationZone(CODO_POSTERIOR_IZQUIERDO, 0.23f, 0.38f, R.drawable.hombre_posterior_codo_izq_, R.drawable.hombre_posterior_codo_izq));
            map_body_back_parts_men.add(new LocationZone(PLIEGUE_POPLITEO_DERECHO, 0.6f, 0.73f, R.drawable.pliegue_der_, R.drawable.pliegue_der));
            map_body_back_parts_men.add(new LocationZone(PLIEGUE_POPLITEO_IZQUIERDO, 0.4f, 0.73f, R.drawable.pliegue_izq_, R.drawable.pliegue_izq));
            map_body_back_parts_men.add(new LocationZone(PANTORRILLA_POSTERIOR_DERECHA, 0.6f, 0.85f, R.drawable.pantorrilla_der_, R.drawable.pantorrilla_der));
            map_body_back_parts_men.add(new LocationZone(PANTORRILLA_POSTERIOR_IZQUIERDA, 0.4f, 0.85f, R.drawable.pantorrilla_izq_, R.drawable.pantorrilla_izq));
            map_body_back_parts_men.add(new LocationZone(TALON_DERECHO, 0.578f, 0.97f, R.drawable.talon_der_, R.drawable.talon_der));
            map_body_back_parts_men.add(new LocationZone(TALON_IZQUIERDO, 0.42f, 0.97f, R.drawable.talon_izq_, R.drawable.talon_izq));
            map_body_back_parts_men.add(new LocationZone(CABEZA_POSTERIOR, 0.5f, 0.081f, R.drawable.hombre_posterior_cabeza_, R.drawable.hombre_posterior_cabeza));
            map_body_back_parts_men.add(new LocationZone(POSTERIOR_GLUTEO, 0.5f, 0.5f, R.drawable.hombre_posterior_gluteos_, R.drawable.hombre_posterior_gluteos));
            map_body_back_parts_men.add(new LocationZone(MANO_POSTERIOR_DERECHA, 0.91f, 0.545f, R.drawable.hombre_posterior_mano_der_, R.drawable.hombre_posterior_mano_der));
            map_body_back_parts_men.add(new LocationZone(MANO_POSTERIOR_IZQUIERDA, 0.09f, 0.545f, R.drawable.hombre_posterior_mano_izq_, R.drawable.hombre_posterior_mano_izq));
            map_body_back_parts_men.add(new LocationZone(ESPALDA, 0.5f, 0.3f, R.drawable.hombre_posterior_espalda_, R.drawable.hombre_posterior_espalda));
        }
        return map_body_back_parts_men;
    }

    private static List<LocationZone> getBodyWoman()
    {
        if(map_body_parts_woman.size()<1) {
            map_body_parts_woman.add(new LocationZone(CABEZA, 0.5f, 0.079f, R.drawable.face2, R.drawable.face1));
            map_body_parts_woman.add(new LocationZone(TORZO, 0.5f, 0.3f, R.drawable.torso_pequeno, R.drawable.torso_pequeno));
            map_body_parts_woman.add(new LocationZone(ABDOMEN, 0.5f, 0.354f, R.drawable.torzo_16, R.drawable.torzo_16));
            map_body_parts_woman.add(new LocationZone(ZONA_GENITAL, 0.5f, 0.458f, R.drawable.zona_genital_mujer_1, R.drawable.zona_genital_mujer));
            map_body_parts_woman.add(new LocationZone(MANO_DERECHA, 0.765f, 0.547f, R.drawable.palma_der_peq, R.drawable.palma_der_peq));
            map_body_parts_woman.add(new LocationZone(MANO_IZQUIERDA, 0.23f, 0.547f, R.drawable.palma_izq_peq, R.drawable.palma_izq_peq));
            map_body_parts_woman.add(new LocationZone(PIE_DERECHO, 0.565f, 0.96f, R.drawable.pie_der_1, R.drawable.pie_der));
            map_body_parts_woman.add(new LocationZone(PIE_IZQUIERDO, 0.435f, 0.96f, R.drawable.pie_izq_1, R.drawable.pie_izq));
            map_body_parts_woman.add(new LocationZone(HOMBRO_IZQUIERDO, 0.6732f, 0.25f, R.drawable.hombro_der_1, R.drawable.hombro_der));
            map_body_parts_woman.add(new LocationZone(HOMBRO_DERECHO, 0.33f, 0.25f, R.drawable.hombro_izq_1, R.drawable.hombro_izq));
            map_body_parts_woman.add(new LocationZone(ANTEBRAZO_IZQUIERDO, 0.71f, 0.395f, R.drawable.antebrazo_der_1, R.drawable.antebrazo_der));
            map_body_parts_woman.add(new LocationZone(ANTEBRAZO_DERECHO, 0.295f, 0.395f, R.drawable.antebrazo_izq_1, R.drawable.antebrazo_izq));
            map_body_parts_woman.add(new LocationZone(MUNECA_IZQUIERDA, 0.74f, 0.49f, R.drawable.muneca_der_1, R.drawable.muneca_der));
            map_body_parts_woman.add(new LocationZone(MUNECA_DERECHO, 0.26f, 0.49f, R.drawable.muneca_izq_1, R.drawable.muneca_izq));
            map_body_parts_woman.add(new LocationZone(CARA_ANTERIOR_MUSLO, 0.59f, 0.5705f, R.drawable.muslo_der_1, R.drawable.muslo_der));
            map_body_parts_woman.add(new LocationZone(CARA_ANTERIOR_MUSLO_DERECHO, 0.410f, 0.5705f, R.drawable.muslo_izq_1, R.drawable.muslo_izq));
            map_body_parts_woman.add(new LocationZone(RODILLA_IZQUIERDA, 0.565f, 0.735f, R.drawable.rodilla_der_1, R.drawable.rodilla_der));
            map_body_parts_woman.add(new LocationZone(RODILLA_DERECHO, 0.435f, 0.735f, R.drawable.rodilla_izq_1, R.drawable.rodilla_izq));
            map_body_parts_woman.add(new LocationZone(CANILLA_IZQUIERDA, 0.564f, 0.855f, R.drawable.canilla_der_1, R.drawable.canilla_der));
            map_body_parts_woman.add(new LocationZone(CANILLA_DERECHA, 0.435f, 0.855f, R.drawable.canilla_izq_1, R.drawable.canilla_izq));
        }
        return map_body_parts_woman;
    }

    private static List<LocationZone> getBackBodyWoman() {
        if(map_body_back_parts_woman.size()<1) {
            map_body_back_parts_woman.add(new LocationZone(CABEZA_POSTERIOR, 0.5f, 0.079f, R.drawable.cabeza_es, R.drawable.cabeza_es));
            map_body_back_parts_woman.add(new LocationZone(POSTERIOR_GLUTEO, 0.5f, 0.458f, R.drawable.gluteos_, R.drawable.gluteos));
            map_body_back_parts_woman.add(new LocationZone(MANO_POSTERIOR_DERECHA, 0.9f, 0.542f, R.drawable.mano_der_es, R.drawable.mano_der_es));
            map_body_back_parts_woman.add(new LocationZone(MANO_POSTERIOR_IZQUIERDA, 0.09f, 0.542f, R.drawable.mano_izq_es, R.drawable.mano_izq_es));
            map_body_back_parts_woman.add(new LocationZone(ESPALDA, 0.5f, 0.28f, R.drawable.espalda_, R.drawable.espalda));
            map_body_back_parts_woman.add(new LocationZone(HOMBRO_DERECHO_ESPALDA, 0.76f, 0.25f, R.drawable.hombro_der_1, R.drawable.hombro_der));
            map_body_back_parts_woman.add(new LocationZone(HOMBRO_IZQUIERDO_ESPALDA, 0.24f, 0.25f, R.drawable.hombro_izq_1, R.drawable.hombro_izq));
            map_body_back_parts_woman.add(new LocationZone(ANTEBRAZO_DERECHO_ESPALDA, 0.85f, 0.45f, R.drawable.antebrazo_der_1, R.drawable.antebrazo_der));
            map_body_back_parts_woman.add(new LocationZone(ANTEBRAZO_IZQUIERDO_ESPALDA, 0.14f, 0.45f, R.drawable.antebrazo_izq_1, R.drawable.antebrazo_izq));
            map_body_back_parts_woman.add(new LocationZone(CODO_POSTERIOR_DERECHO, 0.8f, 0.345f, R.drawable.codo_der, R.drawable.codo_der));
            map_body_back_parts_woman.add(new LocationZone(CODO_POSTERIOR_IZQUIERDO, 0.19f, 0.345f, R.drawable.codo_izq, R.drawable.codo_izq));
            map_body_back_parts_woman.add(new LocationZone(CARA_POSTERIOR_MUSLO_DERECHO, 0.64f, 0.6f, R.drawable.muslo_der_, R.drawable.posteriror_muslo_der));
            map_body_back_parts_woman.add(new LocationZone(CARA_POSTERIOR_MUSLO_IZQUIERDO, 0.35f, 0.6f, R.drawable.muslo_izq_, R.drawable.posterior_muslo_izq));
            map_body_back_parts_woman.add(new LocationZone(PLIEGUE_POPLITEO_DERECHO, 0.6f, 0.73f, R.drawable.pliegue_der_, R.drawable.pliegue_der));
            map_body_back_parts_woman.add(new LocationZone(PLIEGUE_POPLITEO_IZQUIERDO, 0.4f, 0.73f, R.drawable.pliegue_izq_, R.drawable.pliegue_izq));
            map_body_back_parts_woman.add(new LocationZone(PANTORRILLA_POSTERIOR_DERECHA, 0.6f, 0.85f, R.drawable.pantorrilla_der_, R.drawable.pantorrilla_der));
            map_body_back_parts_woman.add(new LocationZone(PANTORRILLA_POSTERIOR_IZQUIERDA, 0.39f, 0.85f, R.drawable.pantorrilla_izq_, R.drawable.pantorrilla_izq));
            map_body_back_parts_woman.add(new LocationZone(TALON_DERECHO, 0.576f, 0.97f, R.drawable.talon_der_, R.drawable.talon_der));
            map_body_back_parts_woman.add(new LocationZone(TALON_IZQUIERDO, 0.42f, 0.97f, R.drawable.talon_izq_, R.drawable.talon_izq));
        }
        return map_body_back_parts_woman;
    }

    private static List<LocationZone> getTorsoMen()
    {
        if(list_torso_right_body_parts_men.size()<1) {
            list_torso_right_body_parts_men.add(new LocationZone(AXILA_IZQUIERDA, 0.91f, 0.19f, R.drawable.hombre_axila_der_, R.drawable.hombre_axila_der));
            list_torso_right_body_parts_men.add(new LocationZone(AXILA_DERECHO, 0.09f, 0.19f, R.drawable.hombre_axila_izq_, R.drawable.hombre_axila_izq));
            list_torso_right_body_parts_men.add(new LocationZone(TETILLA_IZQUIERDA, 0.795f, 0.333f, R.drawable.hombre_tetilla_der_, R.drawable.hombre_tetilla_der));
            list_torso_right_body_parts_men.add(new LocationZone(TETILLA_DERECHA, 0.21f, 0.333f, R.drawable.hombre_tetilla_izq_, R.drawable.hombre_tetilla_izq));
            list_torso_right_body_parts_men.add(new LocationZone(REGION_SUPRACLAVICULAR, 0.5f, 0.08f, R.drawable.hombre_supraclavicular_, R.drawable.hombre_supraclavicular));
            list_torso_right_body_parts_men.add(new LocationZone(REGION_PREESTERNAL, 0.5f, 0.25f, R.drawable.hombre_preesternal_, R.drawable.hombre_preesternal));
            list_torso_right_body_parts_men.add(new LocationZone(TORAX_ANTERIOR, 0.5f, 0.4f, R.drawable.hombre_torax_anterior_, R.drawable.hombre_torax_anterior));
            list_torso_right_body_parts_men.add(new LocationZone(ABDOMEN, 0.5f, 0.8f, R.drawable.hombre_abdomen_, R.drawable.hombre_abdomen));
            list_torso_right_body_parts_men.add(new LocationZone(COSTILLAS_IZQUIERDAS, 0.81f, 0.41f, R.drawable.hombre_costilla_der_, R.drawable.hombre_costilla_der));
            list_torso_right_body_parts_men.add(new LocationZone(COSTILLAS_DERECHA, 0.186f, 0.41f, R.drawable.hombre_costilla_izq_, R.drawable.hombre_costilla_izq));
        }
        return list_torso_right_body_parts_men;
    }

    private static List<LocationZone> getPrivatePartMen()
    {
        if(list_private_body_parts_men.size()<1) {
            list_private_body_parts_men.add(new LocationZone(ANO, 0.5f, 0.75f, R.drawable.hombre_ano_, R.drawable.hombre_ano));
            list_private_body_parts_men.add(new LocationZone(PENE, 0.47f, 0.27f, R.drawable.pene_, R.drawable.pene));
            list_private_body_parts_men.add(new LocationZone(ESCROTO, 0.501f, 0.43f, R.drawable.hombre_escroto_, R.drawable.hombre_escroto));
            list_private_body_parts_men.add(new LocationZone(MUSLO_GENITAL_IZQUIERDO, 0.76f, 0.495f, R.drawable.hombre_muslo_genitales_der_, R.drawable.hombre_muslo_genitales_der));
            list_private_body_parts_men.add(new LocationZone(MUSLO_GENITAL_DERECHO, 0.24f, 0.495f, R.drawable.hombre_muslo_genitales_izq_, R.drawable.hombre_muslo_genitales_izq));
        }
         return list_private_body_parts_men;
    }

    private static List<LocationZone> getFaceMen()
    {
        if(list_face_body_parts_men.size()<1) {
            list_face_body_parts_men.add(new LocationZone(VERTEX, 0.501f, 0.073f, R.drawable.hombre_cabeza1_, R.drawable.hombre_cabeza1));
            list_face_body_parts_men.add(new LocationZone(REGION_FRONTAL_CUERO_CABELLUDO, 0.50f, 0.187f, R.drawable.hombre_cabeza2_, R.drawable.hombre_cabeza2));
            list_face_body_parts_men.add(new LocationZone(REGION_FRONTOTEMPORAL_IZQUIERDO, 0.73f, 0.268f, R.drawable.hombre_cabeza3_, R.drawable.hombre_cabeza3));
            list_face_body_parts_men.add(new LocationZone(REGION_FRONTOTEMPORAL_DERECHA, 0.273f, 0.268f, R.drawable.hombre_cabeza4_, R.drawable.hombre_cabeza4));
            list_face_body_parts_men.add(new LocationZone(REGION_TEMPORAL_IZQUIERDA, 0.76f, 0.38f, R.drawable.hombre_temporal_der_, R.drawable.hombre_temporal_der));
            list_face_body_parts_men.add(new LocationZone(REGION_TEMPORAL_DERECHA, 0.24f, 0.38f, R.drawable.hombre_temporal_izq_, R.drawable.hombre_temporal_izq));
            list_face_body_parts_men.add(new LocationZone(REGION_SUPRACILIAR_IZQUIERDA, 0.643f, 0.36f, R.drawable.hombre_supraciliar_der_, R.drawable.hombre_supraciliar_der));
            list_face_body_parts_men.add(new LocationZone(REGION_SUPRACILIAR_DERECHA, 0.355f, 0.36f, R.drawable.hombre_supraciliar_izq_, R.drawable.hombre_supraciliar_izq));
            list_face_body_parts_men.add(new LocationZone(FRENTE, 0.50f, 0.31f, R.drawable.hombre_cabeza5_, R.drawable.hombre_cabeza5));
            list_face_body_parts_men.add(new LocationZone(CUELLO, 0.499f, 0.85f, R.drawable.hombre_cuello_, R.drawable.hombre_cuello));
            list_face_body_parts_men.add(new LocationZone(MENTON, 0.5f, 0.78f, R.drawable.hombre_barbilla_, R.drawable.hombre_barbilla));
            list_face_body_parts_men.add(new LocationZone(MEJILLA_IZQUIERDA, 0.666f, 0.572f, R.drawable.hombre_mejilla_der_, R.drawable.hombre_mejilla_der));
            list_face_body_parts_men.add(new LocationZone(MEJILLA_DERECHA, 0.3335f, 0.572f, R.drawable.hombre_mejilla_izq_, R.drawable.hombre_mejilla_izq));
            list_face_body_parts_men.add(new LocationZone(DORSO_NASAL, 0.5f, 0.54f, R.drawable.hombre_dorso_nasal_, R.drawable.hombre_dorso_nasal));
            list_face_body_parts_men.add(new LocationZone(PUNTA_NASAL, 0.5f, 0.60f, R.drawable.hombre_punta_nariz_, R.drawable.hombre_punta_nariz));
            list_face_body_parts_men.add(new LocationZone(VERTIENTE_NASAL_IZQUIERDO, 0.555f, 0.545f, R.drawable.hombre_vertice_nariz_der_, R.drawable.hombre_vertice_nariz_der));
            list_face_body_parts_men.add(new LocationZone(VERTIENTE_NASAL_DERECHO, 0.4405f, 0.545f, R.drawable.hombre_vertice_nariz_izq_, R.drawable.hombre_vertice_nariz_izq));
            list_face_body_parts_men.add(new LocationZone(LABIO_SUPERIOR, 0.5f, 0.6713f, R.drawable.hombre_labio_sup_, R.drawable.hombre_labio_sup));
            list_face_body_parts_men.add(new LocationZone(LABIO_INFERIOR, 0.5f, 0.705f, R.drawable.hombre_labio_inf_, R.drawable.hombre_labio_inf));
            list_face_body_parts_men.add(new LocationZone(REGION_MAXILAR_IZQUIERDO, 0.69f, 0.7f, R.drawable.hombre_maxilar_der_, R.drawable.hombre_maxilar_der));
            list_face_body_parts_men.add(new LocationZone(REGION_MAXILAR_DERECHO, 0.31f, 0.7f, R.drawable.hombre_maxilar_izq_, R.drawable.hombre_maxilar_izq));
            list_face_body_parts_men.add(new LocationZone(ALA_NASAL_DERECHA, 0.457f, 0.59f, R.drawable.hombre_orificio_izq_, R.drawable.hombre_orificio_izq));
            list_face_body_parts_men.add(new LocationZone(ALA_NASAL_IZQUIERDA, 0.538f, 0.59f, R.drawable.hombre_orificio_der_, R.drawable.hombre_orificio_der));
            list_face_body_parts_men.add(new LocationZone(PABELLON_AURICULAR_DERECHO, 0.19f, 0.5f, R.drawable.hombre_oreja_izq_, R.drawable.hombre_oreja_izq));
            list_face_body_parts_men.add(new LocationZone(PABELLON_AURICULAR_IZQUIERDO, 0.81f, 0.5f, R.drawable.hombre_oreja_der_, R.drawable.hombre_oreja_der));
            list_face_body_parts_men.add(new LocationZone(REGION_PREAURICULAR_DERECHO, 0.24f, 0.52f, R.drawable.hombre_preauricular_izq_, R.drawable.hombre_preauricular_izq));
            list_face_body_parts_men.add(new LocationZone(REGION_PREAURICULAR_IZQUIERDA, 0.75f, 0.52f, R.drawable.hombre_preauricular_der_, R.drawable.hombre_preauricular_der));
            list_face_body_parts_men.add(new LocationZone(PARPADO_INFERIOR_IZQUIERDO, 0.666f, 0.47f, R.drawable.hombre_parpado_inf_der_, R.drawable.hombre_parpado_inf_der));
            list_face_body_parts_men.add(new LocationZone(PARPADO_INFERIOR_DERECHO, 0.3335f, 0.47f, R.drawable.hombre_parpado_inf_izq_, R.drawable.hombre_parpado_inf_izq));
            list_face_body_parts_men.add(new LocationZone(PARPADO_SUPERIOR_IZQUIERDO, 0.65f, 0.42f, R.drawable.hombre_parpado_sup_der_, R.drawable.hombre_parpado_sup_der));
            list_face_body_parts_men.add(new LocationZone(PARPADO_SUPERIOR_DERECHO, 0.34f, 0.42f, R.drawable.hombre_parpado_sup_izq_, R.drawable.hombre_parpado_sup_izq));
            list_face_body_parts_men.add(new LocationZone(CEJA_IZQUIERDA, 0.65f, 0.385f, R.drawable.hombre_ceja_der_, R.drawable.hombre_ceja_der));
            list_face_body_parts_men.add(new LocationZone(CEJA_DERECHA, 0.35f, 0.385f, R.drawable.hombre_ceja_izq_, R.drawable.hombre_ceja_izq));
            list_face_body_parts_men.add(new LocationZone(FOSA_NASAL, 0.5f, 0.62f, R.drawable.hombre_fosas_nasales_, R.drawable.hombre_fosas_nasales));
            list_face_body_parts_men.add(new LocationZone(PIEL_DE_LABIO_SUPERIOR, 0.5f, 0.65f, R.drawable.hombre_superior_al_labio_, R.drawable.hombre_superior_al_labio));
            list_face_body_parts_men.add(new LocationZone(PESTANA_IZQUIERDA, 0.63f, 0.4477f, R.drawable.hombre_ojo_der_, R.drawable.hombre_ojo_der));
            list_face_body_parts_men.add(new LocationZone(PESTANA_DERECHA, 0.37f, 0.4477f, R.drawable.hombre_ojo_izq_, R.drawable.hombre_ojo_izq));
        }
        return list_face_body_parts_men;
    }

    private static  List<LocationZone> getFaceMenBack()
    {
        if(list_face_back_body_parts_men.size()<1) {
            list_face_back_body_parts_men.add(new LocationZone(NUCA, 0.5f, 0.85f, R.drawable.hombre_nuca_, R.drawable.hombre_nuca));
            list_face_back_body_parts_men.add(new LocationZone(REGION_OCCIPITAL, 0.5f, 0.51f, R.drawable.hombre_region_occipital_, R.drawable.hombre_region_occipital));
            list_face_back_body_parts_men.add(new LocationZone(REGION_RETROAURICULAR_IZQUIERDA, 0.09f, 0.51f, R.drawable.hombre_region_retroauricular_izq_, R.drawable.hombre_region_retroauricular_izq));
            list_face_back_body_parts_men.add(new LocationZone(REGION_RETROAURICULAR_DERECHA, 0.91f, 0.51f, R.drawable.hombre_region_retroauricular_der_, R.drawable.hombre_region_retroauricular_der));
        }
        return list_face_back_body_parts_men;
    }

    private static List<LocationZone> getFaceWoman()
    {
        if(list_face_body_parts_woman.size()<1) {
            list_face_body_parts_woman.add(new LocationZone(VERTEX, 0.501f, 0.073f, R.drawable.cabeza1, R.drawable.cabeza1_));
            list_face_body_parts_woman.add(new LocationZone(REGION_FRONTAL_CUERO_CABELLUDO, 0.50f, 0.187f, R.drawable.cabeza2, R.drawable.cabeza2_));
            list_face_body_parts_woman.add(new LocationZone(REGION_FRONTOTEMPORAL_IZQUIERDO, 0.733f, 0.268f, R.drawable.cabeza3, R.drawable.cabeza3_));
            list_face_body_parts_woman.add(new LocationZone(REGION_FRONTOTEMPORAL_DERECHA, 0.2665f, 0.268f, R.drawable.cabeza4, R.drawable.cabeza4_));
            list_face_body_parts_woman.add(new LocationZone(REGION_TEMPORAL_IZQUIERDA, 0.76f, 0.38f, R.drawable.temporal_der_1, R.drawable.temporal_der));
            list_face_body_parts_woman.add(new LocationZone(REGION_TEMPORAL_DERECHA, 0.24f, 0.38f, R.drawable.temporal_izq_1, R.drawable.temporal_izq));
            list_face_body_parts_woman.add(new LocationZone(REGION_SUPRACILIAR_IZQUIERDA, 0.643f, 0.33f, R.drawable.supraciliar_der_1, R.drawable.supraciliar_der));
            list_face_body_parts_woman.add(new LocationZone(REGION_SUPRACILIAR_DERECHA, 0.355f, 0.33f, R.drawable.supraciliar_izq_1, R.drawable.supraciliar_izq));
            list_face_body_parts_woman.add(new LocationZone(FRENTE, 0.50f, 0.33f, R.drawable.cabeza5_1, R.drawable.cabeza5));
            list_face_body_parts_woman.add(new LocationZone(CUELLO, 0.499f, 0.85f, R.drawable.cuello, R.drawable.cuello_));
            list_face_body_parts_woman.add(new LocationZone(MENTON, 0.5f, 0.778f, R.drawable.menton, R.drawable.menton_));
            list_face_body_parts_woman.add(new LocationZone(MEJILLA_IZQUIERDA, 0.666f, 0.572f, R.drawable.mejilla_der, R.drawable.mejilla_der_));
            list_face_body_parts_woman.add(new LocationZone(MEJILLA_DERECHA, 0.3335f, 0.572f, R.drawable.mejilla_izq, R.drawable.mejilla_izq_));
            list_face_body_parts_woman.add(new LocationZone(DORSO_NASAL, 0.5f, 0.52f, R.drawable.dorsonasal, R.drawable.dorsonasal_));
            list_face_body_parts_woman.add(new LocationZone(PUNTA_NASAL, 0.5f, 0.60f, R.drawable.puntanariz, R.drawable.puntanariz_));
            list_face_body_parts_woman.add(new LocationZone(VERTIENTE_NASAL_IZQUIERDO, 0.555f, 0.545f, R.drawable.vertice_der, R.drawable.vertice_der_));
            list_face_body_parts_woman.add(new LocationZone(VERTIENTE_NASAL_DERECHO, 0.4405f, 0.545f, R.drawable.vertice_izq, R.drawable.vertice_izq_));
            list_face_body_parts_woman.add(new LocationZone(LABIO_SUPERIOR, 0.5f, 0.6718f, R.drawable.labio_sup, R.drawable.labio_sup_));
            list_face_body_parts_woman.add(new LocationZone(LABIO_INFERIOR, 0.5f, 0.705f, R.drawable.labio_inf, R.drawable.labio_inf_));
            list_face_body_parts_woman.add(new LocationZone(REGION_MAXILAR_IZQUIERDO, 0.69f, 0.68f, R.drawable.maxilar_der, R.drawable.maxilar_der_));
            list_face_body_parts_woman.add(new LocationZone(REGION_MAXILAR_DERECHO, 0.31f, 0.68f, R.drawable.maxilar_izq, R.drawable.maxilar_izq_));
            list_face_body_parts_woman.add(new LocationZone(PABELLON_AURICULAR_DERECHO, 0.2f, 0.53f, R.drawable.oreja_izq, R.drawable.oreja_izq_));
            list_face_body_parts_woman.add(new LocationZone(PABELLON_AURICULAR_IZQUIERDO, 0.8f, 0.53f, R.drawable.oreja_der, R.drawable.oreja_der_));
            list_face_body_parts_woman.add(new LocationZone(REGION_PREAURICULAR_DERECHO, 0.235f, 0.52f, R.drawable.preauricular_izq, R.drawable.preauricular_izq_));
            list_face_body_parts_woman.add(new LocationZone(REGION_PREAURICULAR_IZQUIERDA, 0.766f, 0.52f, R.drawable.preauricular_der, R.drawable.preauricular_der_));
            list_face_body_parts_woman.add(new LocationZone(PARPADO_INFERIOR_IZQUIERDO, 0.666f, 0.45f, R.drawable.parpado_inf_der, R.drawable.parpado_inf_der_));
            list_face_body_parts_woman.add(new LocationZone(PARPADO_INFERIOR_DERECHO, 0.3335f, 0.45f, R.drawable.parpado_inf_izq, R.drawable.parpado_inf_izq_));
            list_face_body_parts_woman.add(new LocationZone(PARPADO_SUPERIOR_IZQUIERDO, 0.666f, 0.4f, R.drawable.parpado_sup_izq, R.drawable.parpado_sup_izq_));
            list_face_body_parts_woman.add(new LocationZone(PARPADO_SUPERIOR_DERECHO, 0.3335f, 0.4f, R.drawable.parpado_sup_der, R.drawable.parpado_sup_der_));
            list_face_body_parts_woman.add(new LocationZone(CEJA_IZQUIERDA, 0.65f, 0.355f, R.drawable.ceja_der, R.drawable.ceja_der_));
            list_face_body_parts_woman.add(new LocationZone(CEJA_DERECHA, 0.35f, 0.355f, R.drawable.ceja_izq, R.drawable.ceja_izq_));
            list_face_body_parts_woman.add(new LocationZone(FOSA_NASAL, 0.5f, 0.61f, R.drawable.fosasnasales, R.drawable.fosasnasales_));
            list_face_body_parts_woman.add(new LocationZone(PIEL_DE_LABIO_SUPERIOR, 0.5f, 0.65f, R.drawable.superior_al_labio, R.drawable.superiorallabio_));
//
        }
        return list_face_body_parts_woman;
    }

    private static List<LocationZone> getFaceBackWoman() {
        if(list_face_back_body_parts_woman.size()<1) {
            list_face_back_body_parts_woman.add(new LocationZone(NUCA, 0.5f, 0.83f, R.drawable.nuca_, R.drawable.nuca));
            list_face_back_body_parts_woman.add(new LocationZone(REGION_OCCIPITAL, 0.5f, 0.47f, R.drawable.region_occipital_, R.drawable.region_occipital));
            list_face_back_body_parts_woman.add(new LocationZone(REGION_RETROAURICULAR_IZQUIERDA, 0.23f, 0.51f, R.drawable.region_retroauricular_izq_, R.drawable.region_retroauricular_izq));
            list_face_back_body_parts_woman.add(new LocationZone(REGION_RETROAURICULAR_DERECHA, 0.765f, 0.51f, R.drawable.region_retroauricular_der_, R.drawable.region_retroauricular_der));
        }
        return list_face_back_body_parts_woman;
    }

    private static  List<LocationZone> getHandLeft()
    {
        if(list_hand_left_body_parts_woman.size()<1) {
            list_hand_left_body_parts_woman.add(new LocationZone(PALMA_IZQUIERDA, 0.384f, 0.29f, R.drawable.palma_der_1, R.drawable.palma_der));
            list_hand_left_body_parts_woman.add(new LocationZone(MENIQUE_IZQUIERDO_PALMA, 0.136f, 0.64f, R.drawable.menique_der_1, R.drawable.menique_der));
            list_hand_left_body_parts_woman.add(new LocationZone(ANULAR_IZQUIERDO_PALMA, 0.295f, 0.735f, R.drawable.anular_der_1, R.drawable.anular_der));
            list_hand_left_body_parts_woman.add(new LocationZone(MEDIO_IZQUIERDO_PALMA, 0.473f, 0.768f, R.drawable.medio_der_1, R.drawable.medio_der));
            list_hand_left_body_parts_woman.add(new LocationZone(INDICE_IZQUIERDO_PALMA, 0.67f, 0.70f, R.drawable.indice_der_1, R.drawable.indice_der));
            list_hand_left_body_parts_woman.add(new LocationZone(PULGAR_IZQUIERDO_PALMA, 0.83f, 0.323f, R.drawable.pulgar_der_1, R.drawable.pulgar_der));
            //list_hand_left_body_parts_woman.add(new LocationZone(PULGAR_DERECHO_PALMA, 0.83f, 0.323f, R.drawable.pulgar_der_1, R.drawable.pulgar_der));
        }
        return list_hand_left_body_parts_woman;
    }
    private static  List<LocationZone> getHandLeftBack() {
        if(list_hand_left_back_body_parts_woman.size()<1)
        {
            list_hand_left_back_body_parts_woman.add(new LocationZone(PALMA_POSTERIOR, 0.382f, 0.2775f, R.drawable.dorso_mano_der_1_es, R.drawable.dorso_mano_der_1));
            list_hand_left_back_body_parts_woman.add(new LocationZone(MENIQUE_DERECHO_POSTERIOR, 0.128f, 0.64f, R.drawable.menique_der_1_es, R.drawable.menique_der_es));
            list_hand_left_back_body_parts_woman.add(new LocationZone(MEDIO_DERECHO_POSTERIOR, 0.473f, 0.74f, R.drawable.medio_der_1_es, R.drawable.medio_der_es));
            list_hand_left_back_body_parts_woman.add(new LocationZone(ANULAR_DERECHO_POSTERIOR, 0.29f, 0.72f, R.drawable.anular_der_1_es, R.drawable.anular_der_es));
            list_hand_left_back_body_parts_woman.add(new LocationZone(INDICE_DERECHO_POSTERIOR, 0.675f, 0.678f, R.drawable.indice_der_1_es, R.drawable.indice_der_es));
            list_hand_left_back_body_parts_woman.add(new LocationZone(PULGAR_DERECHO_POSTERIOR, 0.84f, 0.3205f, R.drawable.pulgar_der_1_es, R.drawable.pulgar_der_es));
            list_hand_left_back_body_parts_woman.add(new LocationZone(UNA_PULGAR_DERECHO, 0.965f, 0.405f, R.drawable.unia_pulgar_der_1, R.drawable.unia_pulgar_der));
            list_hand_left_back_body_parts_woman.add(new LocationZone(UNA_MENIQUE_DERECHO, 0.09f, 0.775f, R.drawable.unia_menique_der_1, R.drawable.unia_menique_der));
            list_hand_left_back_body_parts_woman.add(new LocationZone(UNA_MEDIO_DERECHO, 0.52f, 0.95f, R.drawable.unia_medio_der_1, R.drawable.unia_medio_der));
            list_hand_left_back_body_parts_woman.add(new LocationZone(UNA_ANULAR_DERECHO, 0.31f, 0.9f, R.drawable.unia_anular_der_1, R.drawable.unia_anular_der));
            list_hand_left_back_body_parts_woman.add(new LocationZone(UNA_INDICE_DERECHO, 0.75f, 0.865f, R.drawable.unia_indice_der_1, R.drawable.unia_indice_der));

        }
        return list_hand_left_back_body_parts_woman;
    }

    private static  List<LocationZone> getHandRight()
    {
        if(list_hand_right_body_parts_woman.size()<1) {
            list_hand_right_body_parts_woman.add(new LocationZone(PALMA_DERECHA, 0.62f, 0.29f, R.drawable.palma_izq_1, R.drawable.palma_izq));
            list_hand_right_body_parts_woman.add(new LocationZone(MENIQUE_DERECHO_PALMA, 0.875f, 0.66f, R.drawable.menique_izq_1, R.drawable.menique_izq));
            list_hand_right_body_parts_woman.add(new LocationZone(MEDIO_DERECHO_PALMA, 0.525f, 0.775f, R.drawable.medio_izq_1, R.drawable.medio_izq));
            list_hand_right_body_parts_woman.add(new LocationZone(ANULAR_DERECHO_PALMA, 0.715f, 0.745f, R.drawable.anular_izq_1, R.drawable.anular_izq));
            list_hand_right_body_parts_woman.add(new LocationZone(INDICE_DERECHO_PALMA, 0.32f, 0.715f, R.drawable.indice_izq_1, R.drawable.indice_izq));
            list_hand_right_body_parts_woman.add(new LocationZone(PULGAR_DERECHO_PALMA, 0.15f, 0.33f, R.drawable.pulgar_izq_1, R.drawable.pulgar_izq));
            //list_hand_right_body_parts_woman.add(new LocationZone(PULGAR_IZQUIERDO_PALMA, 0.15f, 0.33f, R.drawable.pulgar_izq_1, R.drawable.pulgar_izq));
        }
        return list_hand_right_body_parts_woman;
    }

    private static List<LocationZone> getHandRigthBack()
    {
        if(list_hand_right_back_body_parts_woman.size()<1) {
            list_hand_right_back_body_parts_woman.add(new LocationZone(PALMA_IZQUIERDA_POSTERIOR, 0.62f, 0.27f, R.drawable.dorso_mano_izq_1, R.drawable.dorso_mano_izq_es));
            list_hand_right_back_body_parts_woman.add(new LocationZone(MENIQUE_IZQUIERDO_POSTERIOR, 0.865f, 0.64f, R.drawable.menique_izq_1_es, R.drawable.menique_izq_es));
            list_hand_right_back_body_parts_woman.add(new LocationZone(MEDIO_IZQUIERDO_POSTERIOR, 0.525f, 0.74f, R.drawable.medio_izq_1_es, R.drawable.medio_izq_es));
            list_hand_right_back_body_parts_woman.add(new LocationZone(ANULAR_IZQUIERDO_POSTERIOR, 0.71f, 0.72f, R.drawable.anular_izq_1_es, R.drawable.anular_izq_es));
            list_hand_right_back_body_parts_woman.add(new LocationZone(INDICE_IZQUIERDO_POSTERIOR, 0.325f, 0.67f, R.drawable.indice_izq_1_es, R.drawable.indice_izq_es));
            list_hand_right_back_body_parts_woman.add(new LocationZone(PULGAR_DERECHO_POSTERIOR_MANO, 0.162f, 0.31f, R.drawable.pulgar_izq_1_es, R.drawable.pulgar_izq_es));
            list_hand_right_back_body_parts_woman.add(new LocationZone(UNA_PULGAR_IZQUIERDO, 0.041f, 0.41f, R.drawable.unia_pulgar_izq_1, R.drawable.unia_pulgar_izq));
            list_hand_right_back_body_parts_woman.add(new LocationZone(UNA_MENIQUE_IZQUIERDO, 0.9f, 0.78f, R.drawable.unia_menique_izq_1, R.drawable.unia_menique_izq));
            list_hand_right_back_body_parts_woman.add(new LocationZone(UNA_MEDIO_IZQUIERDO, 0.48f, 0.938f, R.drawable.unia_medio_izq_1, R.drawable.unia_medio_izq));
            list_hand_right_back_body_parts_woman.add(new LocationZone(UNA_ANULAR_IZQUIERDO, 0.68f, 0.89f, R.drawable.unia_anular_izq_1, R.drawable.unia_anular_izq));
            list_hand_right_back_body_parts_woman.add(new LocationZone(UNA_INDICE_IZQUIERDO, 0.26f, 0.86f, R.drawable.unia_indice_izq_1, R.drawable.unia_indice_izq));
        }
        return list_hand_right_back_body_parts_woman;
    }



    private static List<LocationZone> getFootLeft()
    {
        if(list_foot_left_body_parts_woman.size()<1) {
            list_foot_left_body_parts_woman.add(new LocationZone(MALEOLO_INTERNO_Y_EXTERNO_DERECHO, 0.60f, 0.6f, R.drawable.dorso_pie_izq_1, R.drawable.dorso_pie_izq));
            list_foot_left_body_parts_woman.add(new LocationZone(DORSO_DE_PIE_DERECHO, 0.64f, 0.3698f, R.drawable.maleolo_pie_izq_1, R.drawable.maleolo_pie_izq));
            list_foot_left_body_parts_woman.add(new LocationZone(MENIQUE_DERECHO_PLANTA, 0.15f, 0.83f, R.drawable.dedo_1_izq_1, R.drawable.dedo_1_izq));
            list_foot_left_body_parts_woman.add(new LocationZone(ANULAR_DERECHO_PLANTA, 0.26f, 0.86f, R.drawable.dedo_2_izq_1, R.drawable.dedo_2_izq));
            list_foot_left_body_parts_woman.add(new LocationZone(MEDIO_DERECHO_PLANTA, 0.4f, 0.86f, R.drawable.dedo_3_izq_1, R.drawable.dedo_3_izq));
            list_foot_left_body_parts_woman.add(new LocationZone(INDICE_DERECHO_PLANTA, 0.56f, 0.86f, R.drawable.dedo_4_izq_1, R.drawable.dedo_4_izq));
            list_foot_left_body_parts_woman.add(new LocationZone(PULGAR_DERECHO_PLANTA, 0.81f, 0.86f, R.drawable.dedo_5_izq_1, R.drawable.dedo_5_izq));

            list_foot_left_body_parts_woman.add(new LocationZone(UNA_MENIQUE_DERECHO_PIE, 0.07f, 0.88f, R.drawable.una_dedo_1_izq_1, R.drawable.una_dedo_1_izq));
            list_foot_left_body_parts_woman.add(new LocationZone(UNA_ANULAR_DERECHO_PIE, 0.2f, 0.885f, R.drawable.una_dedo_2_izq_1, R.drawable.una_dedo_2_izq));
            list_foot_left_body_parts_woman.add(new LocationZone(UNA_MEDIO_DERECHO_PIE, 0.35f, 0.88f, R.drawable.una_dedo_3_izq_1, R.drawable.una_dedo_3_izq));
            list_foot_left_body_parts_woman.add(new LocationZone(UNA_INDICE_DERECHO_PIE, 0.5f, 0.88f, R.drawable.una_dedo_4_izq_1, R.drawable.una_dedo_4_izq));
            list_foot_left_body_parts_woman.add(new LocationZone(UNA_PULGAR_DERECHO_PIE, 0.81f, 0.86f, R.drawable.una_dedo_5_izq_1, R.drawable.una_dedo_5_izq));
        }
        return list_foot_left_body_parts_woman;
    }

    private static List<LocationZone> getFootRight()
    {
        if(list_foot_right_parts_woman.size()<1) {
            list_foot_right_parts_woman.add(new LocationZone(MALEOLO_INTERNO_Y_EXTERNO_IZQUIERDO, 0.4f, 0.5985f, R.drawable.dorso_pie_der_1, R.drawable.dorso_pie_der));
            list_foot_right_parts_woman.add(new LocationZone(DORSO_DE_PIE_IZQUIERDO, 0.36f, 0.3698f, R.drawable.maleolo_pie_der_1, R.drawable.maleolo_pie_der));
            list_foot_right_parts_woman.add(new LocationZone(MENIQUE_DERECHO_PLANTA, 0.86f, 0.83f, R.drawable.dedo_1_der_1, R.drawable.dedo_1_der));
            list_foot_right_parts_woman.add(new LocationZone(ANULAR_DERECHO_PLANTA, 0.72f, 0.86f, R.drawable.dedo_2_der_1, R.drawable.dedo_2_der));
            list_foot_right_parts_woman.add(new LocationZone(MEDIO_DERECHO_PLANTA, 0.58f, 0.86f, R.drawable.dedo_3_der, R.drawable.dedo_3_der));
            list_foot_right_parts_woman.add(new LocationZone(INDICE_IZQUIERDO_PLANTA, 0.44f, 0.86f, R.drawable.dedo_4_der_1, R.drawable.dedo_4_der));
            list_foot_right_parts_woman.add(new LocationZone(PULGAR_IZQUIERDO_PLANTA, 0.195f, 0.86f, R.drawable.dedo_5_der_1, R.drawable.dedo_5_der));

            list_foot_right_parts_woman.add(new LocationZone(UNA_MENIQUE_IZQUIERDO_PIE, 0.92f, 0.86f, R.drawable.una_dedo_1_der_1, R.drawable.una_dedo_1_der));
            list_foot_right_parts_woman.add(new LocationZone(UNA_ANULAR_IZQUIERDO_PIE, 0.79f, 0.885f, R.drawable.una_dedo_2_der_1, R.drawable.una_dedo_2_der));
            list_foot_right_parts_woman.add(new LocationZone(UNA_MEDIO_IZQUIERDO_PIE, 0.65f, 0.88f, R.drawable.una_dedo_3_der_1, R.drawable.una_dedo_3_der));
            list_foot_right_parts_woman.add(new LocationZone(UNA_INDICE_IZQUIERDO_PIE, 0.48f, 0.88f, R.drawable.una_dedo_4_der_1, R.drawable.una_dedo_4_der));
            list_foot_right_parts_woman.add(new LocationZone(UNA_PULGAR_IZQUIERDO_PIE, 0.195f, 0.86f, R.drawable.una_dedo_5_der_1, R.drawable.una_dedo_5_der));
        }
        return list_foot_right_parts_woman;
    }

    private static List<LocationZone> footPlantLeft()
    {
        if(list_foot_plant_left_parts_woman.size()<1) {
            list_foot_plant_left_parts_woman.add(new LocationZone(MENIQUE_IZQUIERDO_PLANTA, 0.29f, 0.22f, R.drawable.dedo1_izq_1, R.drawable.dedo1_izq));
            list_foot_plant_left_parts_woman.add(new LocationZone(ANULAR_IZQUIERDO_PLANTA, 0.355f, 0.16f, R.drawable.dedo2_izq_1, R.drawable.dedo2_izq));
            list_foot_plant_left_parts_woman.add(new LocationZone(MEDIO_IZQUIERDO_PLANTA, 0.43f, 0.1f, R.drawable.dedo3_izq_1, R.drawable.dedo3_izq));
            list_foot_plant_left_parts_woman.add(new LocationZone(INDICE_IZQUIERDO_PLANTA, 0.51f, 0.0895f, R.drawable.dedo4_izq_1, R.drawable.dedo4_izq));
            list_foot_plant_left_parts_woman.add(new LocationZone(PULGAR_IZQUIERDO_PLANTA, 0.675f, 0.118f, R.drawable.dedo5_izq_1, R.drawable.dedo5_izq));
            list_foot_plant_left_parts_woman.add(new LocationZone(PLANTA_IZQUIERDA, 0.495f, 0.58f, R.drawable.planta_sindedos_izq_1, R.drawable.planta_sindedos_izq));
        }
        return list_foot_plant_left_parts_woman;
    }
    private static List<LocationZone>  getFootPlantRight() {
        if(list_foot_plant_right_parts_woman.size()<1) {
            list_foot_plant_right_parts_woman.add(new LocationZone(MENIQUE_DERECHO_PLANTA, 0.9f, 0.22f, R.drawable.dedo1_der_1, R.drawable.dedo1_der));
            list_foot_plant_right_parts_woman.add(new LocationZone(ANULAR_DERECHO_PLANTA, 0.78f, 0.16f, R.drawable.dedo2_der_1, R.drawable.dedo2_der));
            list_foot_plant_right_parts_woman.add(new LocationZone(MEDIO_DERECHO_PLANTA, 0.63f, 0.11f, R.drawable.dedo3_der_1, R.drawable.dedo3_der));
            list_foot_plant_right_parts_woman.add(new LocationZone(INDICE_DERECHO_PLANTA, 0.45f, 0.0896f, R.drawable.dedo4_der_1, R.drawable.dedo4_der));
            list_foot_plant_right_parts_woman.add(new LocationZone(PULGAR_DERECHO_PLANTA, 0.17f, 0.11f, R.drawable.dedo5_der_1, R.drawable.dedo5_der));
            list_foot_plant_right_parts_woman.add(new LocationZone(PLANTA_DERECHA, 0.51f, 0.58f, R.drawable.planta_sindedos_der_1, R.drawable.planta_sindedos_der));
        }
        return list_foot_plant_right_parts_woman;
    }
    private static List<LocationZone>  getGluteos()
    {
        if(list_gluteos_body_parts_woman.size()<1) {
            list_gluteos_body_parts_woman.add(new LocationZone(GLUTEO_DERECHO, 0.75f, 0.5f, R.drawable.gluteo_der_, R.drawable.gluteo_der));
            list_gluteos_body_parts_woman.add(new LocationZone(GLUTEO_IZQUIERDO, 0.25f, 0.5f, R.drawable.gluteo_izq_, R.drawable.gluteo_izq));
            list_gluteos_body_parts_woman.add(new LocationZone(PERINE, 0.5f, 0.73f, R.drawable.perine_, R.drawable.perine));
            list_gluteos_body_parts_woman.add(new LocationZone(PLIEGUE_INTERGLUTEO, 0.5f, 0.37f, R.drawable.pliegue_intergluteo_, R.drawable.pliegue_intergluteo));
        }
        return list_gluteos_body_parts_woman;
    }
}




