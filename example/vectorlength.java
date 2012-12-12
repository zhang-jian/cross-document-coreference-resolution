package edu.oregonstate.example;

import java.util.*;

import edu.oregonstate.general.DoubleOperation;

public class vectorlength {

	public static void main(String[] args) {
		double[] weight1 = {1.1673309786995496E-4, 1.1495002581103315E-4, 3.837922056010392E-5, 2.0912170829811406E-6, 1.9688502835994023E-5, 1.615922635484047E-5, 7.040631742671628E-5, 0.0, 0.0, 8.15859931431328E-6, -2.8383275348210966E-7, 2.503201238893571E-5, 1.996132054291413E-5, 8.247696154995841E-7, 2.4093964230566593E-6, 1.2473367641814703E-7, 0.0, 8.189147694636297E-6, 0.0, 0.0, 0.0, 2.4548903937245556E-6, -3.7912690163326683E-7, 3.085249302424371E-6, 9.653465398757E-7, -2.1637474480698943E-8, 0.0, 0.0, 0.0, 5.61353935754114E-5, 1.386339966599676E-4, 1.0267888382763229E-4, 3.055787012386773E-5, 5.938715705269249E-5, 6.377431233456318E-5, 7.237605863173623E-5, 3.126533503371333E-5, 2.369404516758495E-5, 6.685028122402004E-5};
		double[] weight2 = {4.200116067696315E-5, 6.460720972805687E-5, 2.223554268938094E-5, 1.5216389156527691E-6, 9.95516472736813E-6, 1.242157079374751E-5, 2.6911358738035826E-5, 0.0, 0.0, -3.6204781356710703E-7, -1.3681841894342816E-7, 1.2493112560819835E-5, 1.2406372624817776E-5, -5.7909190672426866E-8, 4.836372165406123E-8, -1.444547955446108E-7, 0.0, 8.864560858465881E-7, 0.0, 0.0, 0.0, -3.268668488589078E-8, -1.6755126108304571E-7, 1.526639009460901E-6, 5.015193498175165E-7, -1.2090930043970886E-8, 0.0, 0.0, 0.0, 2.838954119009011E-5, 8.090708608972471E-5, 4.444024055329483E-5, 6.70531505329638E-6, 2.000136789128878E-5, 3.067590737346535E-5, 2.95734281204984E-5, 2.4324711511120037E-5, 8.374162572772262E-6, 2.8938974566210145E-5};
		double[] weight3 = {-4.4053105875311874E-5, -4.08342755482055E-5, -1.5443571378454442E-5, -1.3094874217412653E-6, -6.740352581461999E-6, -5.094525637282909E-6, -1.5434280341142602E-5, -0.0, -0.0, -5.26575706598701E-6, 1.0168191552423394E-7, -5.455725172832221E-7, -6.980021840551823E-6, 4.284060400273276E-8, -3.654355134318639E-7, -5.125387920342702E-7, -0.0, -2.405267649315793E-7, -0.0, -0.0, -0.0, 1.562086349440042E-8, 1.4226451223569826E-7, -1.7420125117318885E-6, -9.824317870608981E-8, 5.161518613352476E-10, -0.0, -0.0, -0.0, -1.5658786764825938E-5, -5.095435532253335E-5, -3.508093472224016E-5, 1.7523206971906692E-6, -2.0420903225904417E-5, -1.5189503046013824E-5, -2.8921169960972454E-5, -7.2918393422782546E-6, -1.19057971518527E-5, -1.7653934467126355E-5};
		double[] weight4 = {-7.454338779765993E-5, -6.812743182805998E-5, -2.718146272136056E-5, -1.1808768555601113E-6, -1.7963773562766063E-5, -9.761291651545923E-6, -5.145119559179335E-5, -0.0, -0.0, -6.023630923811408E-6, 1.8514190290537472E-7, -1.8893166213010854E-5, -1.7796222162450238E-5, 8.692108118048915E-8, -1.0187150715541154E-6, 1.7992663856619833E-7, -0.0, -1.7445060998307047E-6, -0.0, -0.0, -0.0, -1.6089092133536981E-6, 2.8510114627048584E-7, -3.820181518004915E-6, -1.3629225534085607E-6, -4.486447785980185E-19, -0.0, -0.0, -0.0, -2.3799021481775425E-5, -8.831123965743119E-5, -7.06196293325403E-5, 4.17644289406311E-6, -5.078769391091795E-5, -2.7530928456127372E-5, -6.250460788401631E-5, -1.4283203170007916E-5, -3.425949176470713E-5, -1.801871312103389E-5};
		double[] weight5 = {-2.3591283466154847E-4, -2.156077961537325E-4, -8.602313511859941E-5, -3.7372061391012475E-6, -5.685124954501477E-5, -3.089226357120787E-5, -1.628313087763453E-4, -0.0, -0.0, -1.906341915125222E-5, 5.859319306229031E-7, -5.9792565464879944E-5, -5.6320987547292574E-5, 2.750854134379828E-7, -3.224001045493156E-6, 5.694268058166239E-7, -0.0, -5.5209642477003105E-6, -0.0, -0.0, -0.0, -5.091831002737061E-6, 9.022801560765843E-7, -1.2090003920599354E-5, -4.313339282707572E-6, 1.2341563451470236E-27, -0.0, -0.0, -0.0, -7.531847942708989E-5, -2.794849482338751E-4, -2.2349503328779612E-4, 1.3217489986746346E-5, -1.607314771767949E-4, -8.712911452800398E-5, -1.9781283973446855E-4, -4.5203083025876634E-5, -1.0842348400510232E-4, -5.702512075667013E-5};
		double[] weight6 = {2.0253990633306424E-4, 1.851072787129417E-4, 7.385404763752541E-5, 3.20852988963848E-6, 4.880890339978492E-5, 2.6522152468326858E-5, 1.397967095557376E-4, 0.0, 0.0, 1.6366651415222428E-5, -5.030442642773989E-7, 5.133413205790397E-5, 4.8353653834814384E-5, -2.3617101609267558E-7, 2.7679243086061566E-6, -4.888740033118382E-7, 0.0, 4.739952292979996E-6, 0.0, 0.0, 0.0, 4.3715255078754245E-6, -7.746409327839766E-7, 1.0379716157273102E-5, 3.703161532333155E-6, 2.9147195885239222E-36, 0.0, 0.0, 0.0, 6.466370424566827E-5, 2.3994817966560065E-4, 1.9187876392124642E-4, -1.1347704705063917E-5, 1.3799392635363545E-4, 7.480357192393595E-5, 1.698296495346504E-4, 3.880852101650843E-5, 9.308557682418116E-5, 4.895817827485992E-5};
		double[] weight7 = {7.085439319157116E-5, 6.475594931391263E-5, 2.583630961837763E-5, 1.1224377579868602E-6, 1.707478439854107E-5, 9.278225971860505E-6, 4.890498472662273E-5, 0.0, 0.0, 5.725534170518397E-6, -1.759796216912579E-7, 1.7958183366578323E-5, 1.6915524762942786E-5, -8.26195407001211E-8, 9.683010170054188E-7, -1.7102244924925056E-7, 0.0, 1.6581741818514294E-6, 0.0, 0.0, 0.0, 1.5292876983592412E-6, -2.709920934963974E-7, 3.6311288137703253E-6, 1.2954743981778996E-6, 2.4081279310476237E-45, 0.0, 0.0, 0.0, 2.2621258243853025E-5, 8.394090318018866E-5, 6.712481322881514E-5, -3.969759567639257E-6, 4.82743183451006E-5, 2.6168481032655095E-5, 5.941138702773488E-5, 1.3576357652527206E-5, 3.2564061967713216E-5, 1.7127005123254916E-5};
		double[] weight8 = {4.2937614216411794E-5, 3.9241969969888344E-5, 1.5656749641060172E-5, 6.801949358887071E-7, 1.0347283665964402E-5, 5.622585551121912E-6, 2.96363185522424E-5, 0.0, 0.0, 3.4696617432304457E-6, -1.0664328301703955E-7, 1.0882621594640248E-5, 1.0250772659581537E-5, -5.006726902208432E-8, 5.867883929388279E-7, -1.0363924687571446E-7, 0.0, 1.0048500892732314E-6, 0.0, 0.0, 0.0, 9.267451495987805E-7, -1.6422064239243666E-7, 2.2004564735206075E-6, 7.850547782662825E-7, 1.2056838886374362E-54, 0.0, 0.0, 0.0, 1.370843522628506E-5, 5.086801192387988E-5, 4.0677496552329857E-5, -2.4056660027607303E-6, 2.9254136042883483E-5, 1.5858044824005337E-5, 3.6003176392487646E-5, 8.227244368209329E-6, 1.9733753506413917E-5, 1.0378929316020313E-5};
		double[] weight9 = {-5.6767947157385726E-5, -5.188192493356707E-5, -2.069983515619739E-5, -8.992877429713496E-7, -1.3680174436599955E-5, -7.433637040130885E-6, -3.918226469299487E-5, -0.0, -0.0, -4.587250085693484E-6, 1.4099340090226708E-7, -1.4387946300524316E-5, -1.3552576948699607E-5, 6.619408493064185E-8, -7.75794675387122E-7, 1.3702175580642852E-7, -0.0, -1.3285152845579813E-6, -0.0, -0.0, -0.0, -1.2252525120661806E-6, 2.1711659857250544E-7, -2.909230032701712E-6, -1.0379232517124648E-6, -7.980914168401472E-64, -0.0, -0.0, -0.0, -1.8123962887503616E-5, -6.725274949702122E-5, -5.3779838887619825E-5, 3.1805381601952655E-6, -3.867698006338284E-5, -2.0965968114839966E-5, -4.7599906334935016E-5, -1.0877264190586796E-5, -2.6090054063618554E-5, -1.3722013244435916E-5};
		double[] weight10 = {-1.184915251077799E-4, -1.082929490802609E-4, -4.32066889848483E-5, -1.8770799634516075E-6, -2.855457725534991E-5, -1.5516202964689958E-5, -8.178499546192658E-5, -0.0, -0.0, -9.574950054079893E-6, 2.942950016621955E-7, -3.0031906836288662E-5, -2.8288243493106995E-5, 1.381666674470402E-7, -1.6193133424793103E-6, 2.86005001615373E-7, -0.0, -2.7730050156620948E-6, -0.0, -0.0, -0.0, -2.5574650144447135E-6, 4.5318666922629214E-7, -6.0724250342974225E-6, -2.166453345569591E-6, 1.1026963145588171E-72, -0.0, -0.0, -0.0, -3.7830080372348E-5, -1.4037641406162315E-4, -1.1225445782243125E-4, 6.63872548042276E-6, -8.073031673256794E-5, -4.376218732026594E-5, -9.935510757464681E-5, -2.270407311664051E-5, -5.445767287606389E-5, -2.864190724344038E-5};
		
		List<double[]> weights = new ArrayList<double[]>();
		weights.add(weight1);
		weights.add(weight2);
		weights.add(weight3);
		weights.add(weight4);
		weights.add(weight5);
		weights.add(weight6);
		weights.add(weight7);
		weights.add(weight8);
		weights.add(weight9);
		weights.add(weight10);

		for (int i = 0; i < weights.size() - 1; i++) {
			double[] differences = DoubleOperation.minus(weights.get(i), weights.get(i+1));
			double sum = 0.0;
			for (double difference : differences) {
				sum += difference * difference;
			}
			double length = Math.sqrt(sum);
			System.out.println(length);
		}
		
		
	}
}