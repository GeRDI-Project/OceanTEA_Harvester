package de.gerdiproject.harvest.harvester.subharvester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import de.gerdiproject.harvest.IDocument;
import de.gerdiproject.harvest.harvester.AbstractListHarvester;
import de.gerdiproject.harvest.oceantea.constants.OceanTeaTimeSeriesDataCiteConstants;
import de.gerdiproject.harvest.oceantea.utils.OceanTeaDownloader;
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

		return OceanTeaDownloader.getAllTimeseries();
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

		// Subjects
		List<Subject> subjects = new ArrayList<>(OceanTeaTimeSeriesDataCiteConstants.SUBJECTS);
		subjects.addAll(timeseriesParser.getSubjectsStrings());
		document.setSubjects(subjects);

		// Descriptions
		List<Description> descriptions = new ArrayList<>(OceanTeaTimeSeriesDataCiteConstants.DESCRIPTIONS);
		descriptions.addAll(timeseriesParser.getDescription());
		document.setDescriptions(descriptions);

		// WebLinks
		List<WebLink> webLinks = new ArrayList<>(OceanTeaTimeSeriesDataCiteConstants.WEB_LINKS);
		webLinks.addAll(timeseriesParser.getWebLinks());
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
