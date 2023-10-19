package ro.linic.cloud.util;

import static ro.linic.cloud.util.PresentationUtils.EMPTY_STRING;
import static ro.linic.cloud.util.StringUtils.isEmpty;
import static ro.linic.cloud.util.StringUtils.stripAccents;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.logging.Logger;
import org.springframework.lang.Nullable;

public class AddressUtils
{
	private static final Logger log = Logger.getLogger(AddressUtils.class);
	
	/**
	 * Key is 2-letter county code as specified by ISO 3166.
	 * Value is full county name.
	 */
	public static final Map<String, String> judete;
	
	static
	{
		judete = Map.ofEntries(
				Map.entry("RO-AB", "Alba"),
				Map.entry("RO-AR", "Arad"),
				Map.entry("RO-AG", "Arges"),
				Map.entry("RO-BC", "Bacau"),
				Map.entry("RO-BH", "Bihor"),
				Map.entry("RO-BN", "Bistrita-Nasaud"),
				Map.entry("RO-BT", "Botosani"),
				Map.entry("RO-BV", "Brasov"),
				Map.entry("RO-BR", "Braila"),
				Map.entry("RO-B", "Bucuresti"),
				Map.entry("RO-BZ", "Buzau"),
				Map.entry("RO-CS", "Caras-Severin"),
				Map.entry("RO-CJ", "Cluj"),
				Map.entry("RO-CT", "Constanta"),
				Map.entry("RO-CV", "Covasna"),
				Map.entry("RO-CL", "Calarasi"),
				Map.entry("RO-DJ", "Dolj"),
				Map.entry("RO-DB", "Dambovita"),
				Map.entry("RO-GL", "Galati"),
				Map.entry("RO-GR", "Giurgiu"),
				Map.entry("RO-GJ", "Gorj"),
				Map.entry("RO-HR", "Harghita"),
				Map.entry("RO-HD", "Hunedoara"),
				Map.entry("RO-IL", "Ialomita"),
				Map.entry("RO-IS", "Iasi"),
				Map.entry("RO-IF", "Ilfov"),
				Map.entry("RO-MM","Maramures"),
				Map.entry("RO-MH", "Mehedinti"),
				Map.entry("RO-MS", "Mures"),
				Map.entry("RO-NT", "Neamt"),
				Map.entry("RO-OT", "Olt"),
				Map.entry("RO-PH", "Prahova"),
				Map.entry("RO-SM", "Satu Mare"),
				Map.entry("RO-SB", "Sibiu"),
				Map.entry("RO-SV", "Suceava"),
				Map.entry("RO-SJ", "Salaj"),
				Map.entry("RO-TR", "Teleorman"),
				Map.entry("RO-TM", "Timis"),
				Map.entry("RO-TL", "Tulcea"),
				Map.entry("RO-VS", "Vaslui"),
				Map.entry("RO-VN", "Vrancea"),
				Map.entry("RO-VL", "Valcea")
				);
	}
	
	/**
	 * @param fullAddress can be null
	 * @return if found returns 2-letter county code as specified by ISO 3166, else an empty string
	 */
	public static String extractCodJudet(@Nullable final String fullAddress)
	{
		if (isEmpty(fullAddress))
			return EMPTY_STRING;
		
		final String normalizedAddress = stripAccents(fullAddress).trim().toUpperCase();
		
		return judete.entrySet().stream()
				.filter(entry -> normalizedAddress.contains(entry.getValue().toUpperCase()))
				.findFirst()
				.map(Entry::getKey)
				.orElse(EMPTY_STRING);
	}
	
	/**
	 * Only works for romanian cities.
	 * 
	 * @param fullAddress
	 * @return the city if found, else fullAddress(never null)
	 */
	public static String extractCity(@Nullable final String fullAddress)
	{
		if (isEmpty(fullAddress))
			return EMPTY_STRING;
		
		try
		{
			String remainingAddress = fullAddress;
			final String normalizedAddress = stripAccents(fullAddress).trim().toUpperCase();
			
			final String foundJudet = judete.entrySet().stream()
					.filter(entry -> normalizedAddress.contains(entry.getValue().toUpperCase()))
					.findFirst()
					.map(Entry::getValue)
					.orElse(EMPTY_STRING);
			
			if (!isEmpty(foundJudet))
			{
				final int startIndex = stripAccents(remainingAddress).toUpperCase().indexOf(foundJudet.toUpperCase());
				final String judetToRemove = remainingAddress.substring(startIndex, startIndex+foundJudet.length());
				remainingAddress = remainingAddress.replaceAll(judetToRemove, EMPTY_STRING);
			}
			
			String foundCity = null;
			final String normalizedRemainingAddress = stripAccents(remainingAddress).trim().toUpperCase();
			final InputStream fileInputStream = CsvParserSimple.class.getClassLoader().getResourceAsStream("orase.csv");
			final CsvParserSimple parser = new CsvParserSimple();
			final List<String[]> lines = parser.readFile(fileInputStream, 1);

			for (final String[] line : lines)
			{
				final String city = line[2];
				
				if (normalizedRemainingAddress.contains(city.trim().toUpperCase()))
				{
					foundCity = city.trim();
					break;
				}
			}
			
			if (!isEmpty(foundCity))
				return foundCity;
			else if (!isEmpty(foundJudet))
				return foundJudet; // in this case city and judet have the same name(eg.: Satu Mare)
		}
		catch (final Exception e)
		{
			log.error(fullAddress, e);
		}
		
		return fullAddress;
	}
	
	/**
	 * Usually the main address line contains the street and number.
	 * 
	 * @param fullAddress
	 * @return the main address line; if city or county could not be extracted, returns the fullAddress(never null)
	 */
	public static String extractAddressLine(@Nullable final String fullAddress)
	{
		if (isEmpty(fullAddress))
			return EMPTY_STRING;
		
		try
		{
			String addressLine = fullAddress;
			final String normalizedAddress = stripAccents(fullAddress).trim().toUpperCase();
			
			final String foundJudet = judete.entrySet().stream()
					.filter(entry -> normalizedAddress.contains(entry.getValue().toUpperCase()))
					.findFirst()
					.map(Entry::getValue)
					.orElse(EMPTY_STRING);
			
			if (!isEmpty(foundJudet))
			{
				final int startIndex = stripAccents(addressLine).toUpperCase().indexOf(foundJudet.toUpperCase());
				final String judetToRemove = addressLine.substring(startIndex, startIndex+foundJudet.length());
				addressLine = addressLine.replaceAll(judetToRemove, EMPTY_STRING);
			}
			
			final String normalizedRemainingAddress = stripAccents(addressLine).trim().toUpperCase();
			String foundCity = null;
			final InputStream fileInputStream = CsvParserSimple.class.getClassLoader().getResourceAsStream("orase.csv");
			final CsvParserSimple parser = new CsvParserSimple();
			final List<String[]> lines = parser.readFile(fileInputStream, 1);

			for (final String[] line : lines)
			{
				final String city = line[2];
				
				if (normalizedRemainingAddress.contains(city.trim().toUpperCase()))
				{
					foundCity = city.trim();
					break;
				}
			}
			
			if (!isEmpty(foundCity))
			{
				final int startIndex = stripAccents(addressLine).toUpperCase().indexOf(foundCity.toUpperCase());
				final String cityToRemove = addressLine.substring(startIndex, startIndex+foundCity.length());
				addressLine = addressLine.replaceAll(cityToRemove, EMPTY_STRING);
			}
			
			return addressLine.trim();
		}
		catch (final Exception e)
		{
			log.error(fullAddress, e);
		}
		
		return fullAddress;
	}
}
