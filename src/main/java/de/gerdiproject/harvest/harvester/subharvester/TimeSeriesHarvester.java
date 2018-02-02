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
 * Harvester for OceanTEA Timeseries data
 *
 * @author Ingo Thomsen
 */
public class TimeSeriesHarvester extends AbstractListHarvester<Timeseries> {

	private final Downloader downloader;

	/**
	 * Default constructor, naming the harvester and ensuring one document per
	 * harvested entry
	 */
	public TimeSeriesHarvester() {
		super("OceanTEA - Timeseries", 1);
		downloader = new Downloader();
	}

	@Override
	protected Collection<Timeseries> loadEntries() {

		return downloader.getAllTimeseries();
	}

	@Override
	protected List<IDocument> harvestEntry(Timeseries timeseries) {

		// parser to obtain non-constant information from the harvested entry
		TimeseriesParser tp = new TimeseriesParser(timeseries);

		// create the document
		DataCiteJson d = new DataCiteJson();

		//
		// derived from constants
		//
		d.setResourceType(OceanTeaTimeSeriesDataCiteConstants.RESOURCE_TYPE);
		d.setPublisher(OceanTeaTimeSeriesDataCiteConstants.PROVIDER);
		d.setRepositoryIdentifier(OceanTeaTimeSeriesDataCiteConstants.REPOSITORY_ID);
		d.setCreators(OceanTeaTimeSeriesDataCiteConstants.CREATORS);
		d.setContributors(OceanTeaTimeSeriesDataCiteConstants.CONTRIBUTORS);
		d.setResearchDisciplines(OceanTeaTimeSeriesDataCiteConstants.DISCIPLINES);
		d.setFormats(OceanTeaTimeSeriesDataCiteConstants.FORMATS);
		List<WebLink> webLinks = new ArrayList<>();
		for (String s : OceanTeaTimeSeriesDataCiteConstants.RELATED_WEB_LINKS) {
			WebLink webLink = new WebLink(s);
			webLink.setType(WebLinkType.Related);
			webLinks.add(webLink);
		}
		d.setWebLinks(webLinks);

		//
		// derived from constants and the harvested entry
		//
		List<Subject> subjects = new ArrayList<>();
		Stream.concat(OceanTeaTimeSeriesDataCiteConstants.SUBJECT_STRINGS.stream(), tp.getSubjectsStrings().stream())
				.forEach(s -> {
					Subject subject = new Subject(s);
					subject.setLang(OceanTeaTimeSeriesDataCiteConstants.LANG);
					subjects.add(new Subject(s));
				});
		d.setSubjects(subjects);

		List<Description> descriptions = new ArrayList<>();
		descriptions.add(OceanTeaTimeSeriesDataCiteConstants.DESCRIPTION_COMMON);
		descriptions.add(tp.getDescription());
		d.setDescriptions(descriptions);

		//
		// derived exclusively from the harvested entry
		//
		d.setResearchDataList(tp.getResearchDataList());
		d.setPublicationYear(tp.getPublicationYear());
		d.setTitles(Arrays.asList(tp.getMainTitle()));

		d.setGeoLocations(tp.getGeoLocations());

		return Arrays.asList(d);
	}
}
