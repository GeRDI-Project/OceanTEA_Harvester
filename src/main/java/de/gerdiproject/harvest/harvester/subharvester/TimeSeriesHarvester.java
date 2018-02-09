package de.gerdiproject.harvest.harvester.subharvester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.harvest.harvester.AbstractListHarvester;
import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDataCiteConstants;
import de.gerdiproject.harvest.oceantea.utils.Downloader;
import de.gerdiproject.harvest.oceantea.utils.Timeseries;
import de.gerdiproject.harvest.oceantea.utils.TimeseriesParser;
import de.gerdiproject.json.datacite.DataCiteJson;
import de.gerdiproject.json.datacite.Description;
import de.gerdiproject.json.datacite.Subject;
import de.gerdiproject.json.datacite.extension.WebLink;
import de.gerdiproject.json.datacite.extension.enums.WebLinkType;

/**
 * Harvester for OceanTEA timeseries data
 *
 * @author Ingo Thomsen
 */
public class TimeSeriesHarvester extends AbstractListHarvester<Timeseries> {

	// parser to harvest non-constant information about timeseries datasets
	private TimeseriesParser timeseriesParser = new TimeseriesParser();
	
	/**
	 * Default constructor, naming the harvester and ensuring one document per
	 * harvested entry
	 */
	public TimeSeriesHarvester() {
		super("OceanTEA - Timeseries", 1);
	}

	@Override
	protected Collection<Timeseries> loadEntries() {

		return Downloader.getAllTimeseries();
	}

	@Override
	protected List<IDocument> harvestEntry(Timeseries timeseries) {

		// specify the Timeseries object for parsing
		timeseriesParser.setTimeseries(timeseries);

		// create the document
		DataCiteJson document = new DataCiteJson();

		//
		// derived from constants
		//
		document.setResourceType(OceanTeaTimeSeriesDataCiteConstants.RESOURCE_TYPE);
		document.setPublisher(OceanTeaTimeSeriesDataCiteConstants.PROVIDER);
		document.setRepositoryIdentifier(OceanTeaTimeSeriesDataCiteConstants.REPOSITORY_ID);
		document.setCreators(OceanTeaTimeSeriesDataCiteConstants.CREATORS);
		document.setContributors(OceanTeaTimeSeriesDataCiteConstants.CONTRIBUTORS);
		document.setResearchDisciplines(OceanTeaTimeSeriesDataCiteConstants.DISCIPLINES);
		document.setFormats(OceanTeaTimeSeriesDataCiteConstants.FORMATS);

		//
		// derived from both constants and the harvested entry
		//
		List<Subject> subjects = new ArrayList<>();
		Stream.concat(OceanTeaTimeSeriesDataCiteConstants.SUBJECT_STRINGS.stream(),
				timeseriesParser.getSubjectsStrings().stream()).forEach(s -> {
					Subject subject = new Subject(s);
					subject.setLang(OceanTeaTimeSeriesDataCiteConstants.LANG);
					subjects.add(new Subject(s));
				});
		document.setSubjects(subjects);

		List<Description> descriptions = new ArrayList<>();
		descriptions.add(timeseriesParser.getDescription());
		descriptions.add(OceanTeaTimeSeriesDataCiteConstants.DESCRIPTION_COMMON);
		document.setDescriptions(descriptions);

		// list of web links associated with the document
		List<WebLink> webLinks = new ArrayList<>();
		for (String s : OceanTeaTimeSeriesDataCiteConstants.RELATED_WEB_LINKS) {
			WebLink webLink = new WebLink(s);
			webLink.setType(WebLinkType.Related);
			webLinks.add(webLink);
		}

		WebLink viewLink = new WebLink(OceanTeaTimeSeriesDataCiteConstants.VIEW_URL);
		viewLink.setType(WebLinkType.ViewURL);
		viewLink.setName(timeseriesParser.getMainTitle().getValue());
		webLinks.add(viewLink);

		document.setWebLinks(webLinks);

		//
		// derived exclusively from the harvested entry
		//
		document.setResearchDataList(timeseriesParser.getResearchDataList());
		document.setPublicationYear(timeseriesParser.getPublicationYear());
		document.setTitles(Arrays.asList(timeseriesParser.getMainTitle()));
		document.setGeoLocations(timeseriesParser.getGeoLocations());
		document.setDates(timeseriesParser.getDates());

		return Arrays.asList(document);
	}
}
