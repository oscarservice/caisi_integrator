package org.oscarehr.caisi_integrator.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.caisi_integrator.util.AdditionalSchemaGenerationSql;
import org.springframework.stereotype.Repository;

@Repository
public class IssueGroupDao extends AbstractDao<IssueGroup>
{
    public IssueGroupDao()
	{
		super(IssueGroup.class);
	}

	public List<IssueGroup> findByName(String name)
	{
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where x.name=?1");
		query.setParameter(1, name);

		@SuppressWarnings("unchecked")
		List<IssueGroup> results=query.getResultList();
		
		return(results);
	}	

	@AdditionalSchemaGenerationSql
	public String[] getAdditionalCustomSql()
	{
		String[] hivCodes = { "B24", "R75", "Z21" };
		String[] cancerCodes = { "C3400", "C3401", "C3409", "C3410", "C3411", "C3419", "C342", "C3430", "C3431", "C3439", "C3480", "C3481", "C3489", "C3490", "C3491", "C3499", "D022", "D143", "D381", "C780", "81700", "81703", "81713", "81803", "C220", "C221", "C222", "C223", "C224", "C227", "C229", "C787", "D015", "D134", "D376", "C182", "C183", "C184", "C185", "C186", "C187", "C188", "C189", "C19", "D010", "D011", "D122", "D123", "D124", "D125", "D126", "D127", "D374", "88500", "97323", "98503", "C009", "C080", "C155", "C259", "C460", "C61", "C679", "C719", "C947", "C950", "D043", "D487", "D489" };
		String[] diabetesCodes = { "E100", "E1010", "E1011", "E1012", "E1020", "E1021", "E1022", "E1028", "E1030", "E1031", "E1032", "E1033", "E1035", "E1038", "E1040", "E1041", "E1042", "E1050", "E1051", "E1052", "E1060", "E1061", "E1062", "E1063", "E1068", "E1070", "E1071", "E1078", "E109", "E110", "E1110", "E1111", "E1112", "E1120", "E1121", "E1122", "E1128", "E1130", "E1131", "E1132", "E1133", "E1135", "E1138", "E1140", "E1141", "E1142", "E1150", "E1151", "E1152", "E1160", "E1161", "E1162", "E1163", "E1168", "E1170", "E1171", "E1178", "E119", "E130", "E1310", "E1311", "E1312", "E1320", "E1321", "E1322", "E1328", "E1330", "E1331", "E1332", "E1333", "E1335", "E1338", "E1340", "E1341", "E1342", "E1350", "E1351", "E1352", "E1360", "E1361", "E1362", "E1363", "E1368", "E1370", "E1371", "E1378", "E139", "E140", "E1410", "E1411", "E1412", "E1420", "E1421", "E1422", "E1428", "E1430", "E1431", "E1432", "E1433", "E1435", "E1438", "E1440", "E1441", "E1442", "E1450", "E1451", "E1452", "E1460", "E1461", "E1462", "E1463", "E1468", "E1470", "E1471", "E1478", "E149", "O24501", "O24502", "O24503", "O24504", "O24509", "O24601", "O24602", "O24603", "O24604", "O24609", "O24701", "O24702", "O24703", "O24704", "O24709" };
		String[] seizureCodes = { "F803", "G400", "G401", "G402", "G403", "G404", "G405", "G406", "G407", "G408", "G409", "G410", "G411", "G412", "G418", "G419" };
		String[] liverCodes = { "B180", "B181", "B182", "B188", "B189", "B190", "K700", "K701", "K702", "K703", "K704", "K709", "K710", "K711", "K712", "K713", "K714", "K715", "K716", "K717", "K718", "K719", "K720", "K721", "K729", "K730", "K731", "K732", "K738", "K739", "K740", "K741", "K742", "K743", "K744", "K745", "K746", "K750", "K751", "K752", "K753", "K754", "K758", "K759", "K760", "K761", "K762", "K763", "K764", "K765", "K766", "K767", "K768", "K769" };
	 	String[] schizophreniaCodes = { "F062", "F200", "F201", "F202", "F203", "F204", "F205", "F206", "F208", "F209", "F21", "F220", "F228", "F229", "F230", "F231", "F232", "F233", "F238", "F239", "F24", "F250", "F251", "F252", "F258", "F259", "F601" };
		String[] bipolarCodes = { "F310", "F311", "F312", "F313", "F314", "F315", "F316", "F317", "F318", "F319" };
		String[] depressionCodes = { "F204", "F320", "F321", "F322", "F323", "F328", "F329", "F330", "F331", "F332", "F333", "F334", "F338", "F339", "F340", "F341", "F412" };
		String[] cognitiveDisabilityCodes = { "F000", "F001", "F002", "F009", "F010", "F011", "F012", "F013", "F018", "F019", "F020", "F021", "F022", "F023", "F024", "F028", "F03", "F04", "F050", "F051", "F058", "F059", "F060", "F061", "F062", "F063", "F064", "F065", "F066", "F067", "F068", "F069", "F070", "F071", "F072", "F078", "F079", "F09", "F700", "F701", "F708", "F709", "F710", "F711", "F718", "F719", "F720", "F721", "F728", "F729", "F730", "F731", "F738", "F739", "F780", "F781", "F788", "F789", "F790", "F791", "F798", "F799", "F83", "F840", "F841", "F842", "F843", "F844", "F845", "F848", "F849", "F88", "F89", "F99", "G300", "G301", "G308", "G309", "G310", "G311", "G312", "G318", "G319", "G320", "G328" };
		String[] pneumoniaCodes = { "J120", "J121", "J122", "J128", "J129", "J13", "J14", "J150", "J151", "J152", "J153", "J154", "J155", "J156", "J157", "J158", "J159", "J160", "J168", "J170", "J171", "J172", "J173", "J178", "J180", "J181", "J182", "J188", "J189", "B052", "B012", "J851" };
		String[] influenzaCodes = { "J09", "J100", "J101", "J108", "J110", "J111", "J118" };

		ArrayList<String> sql=new ArrayList<String>();
		String insertLine="insert into "+modelClass.getSimpleName()+" (name,codeType,issueCode) values ";
		
		for (String temp : hivCodes)
		{
			sql.add(insertLine + "('HIV','ICD10','" + temp + "')");
		}

		for (String temp : cancerCodes)
		{
			sql.add(insertLine + "('CANCER','ICD10','" + temp + "')");
		}

		for (String temp : diabetesCodes)
		{
			sql.add(insertLine + "('DIABETES','ICD10','" + temp + "')");
		}

		for (String temp : seizureCodes)
		{
			sql.add(insertLine + "('SEIZURE','ICD10','" + temp + "')");
		}

		for (String temp : liverCodes)
		{
			sql.add(insertLine + "('LIVER_DISEASE','ICD10','" + temp + "')");
		}

		for (String temp : schizophreniaCodes)
		{
			sql.add(insertLine + "('SCHIZOPHRENIA','ICD10','" + temp + "')");
		}

		for (String temp : bipolarCodes)
		{
			sql.add(insertLine + "('BIPOLAR','ICD10','" + temp + "')");
		}

		for (String temp : depressionCodes)
		{
			sql.add(insertLine + "('DEPRESSION','ICD10','" + temp + "')");
		}

		for (String temp : cognitiveDisabilityCodes)
		{
			sql.add(insertLine + "('COGNITIVE_DISABILITY','ICD10','" + temp + "')");
		}

		for (String temp : pneumoniaCodes)
		{
			sql.add(insertLine + "('PNEUMONIA','ICD10','" + temp + "')");
		}

		for (String temp : influenzaCodes)
		{
			sql.add(insertLine + "('INFLUENZA','ICD10','" + temp + "')");
		}
		
		return(sql.toArray(new String[0]));
	}
}
