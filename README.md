This project aims to investigate the "Weather influence on Human Mobility" based on tweets with geo tags within the Netherlands.
It uses a java crawler to retrieve the Twitter data through the official Streaming API.

Then Hadoop/Pig is used to process it. It's totally overkill given the small size of the dataset but it's ok from a learning perspective.
In addition to massaging the raw data to extract meaningful information the program also generates KLM files used to draw information on Google Earth/Maps.

For more information please see `Presentation.pdf`.

This project was done for TU Delft course: Measuring and Simulating the Internet (ET4285).
The contributors are Henrique Dantas, Emanuel Dias, Liang Huo, Carryoles Maria and Vadim Weis.
