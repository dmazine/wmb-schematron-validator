<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron">
	<ns uri="http://www.topologi.com/example" prefix="ex" />
	<pattern>
		<title>Check structure</title>
		<rule context="ex:Person">
			<assert test="@Title">
				The element Person must have a Title attribute
			</assert>
			<assert
				test="count(ex:*) = 2 and count(ex:Name) = 1 and count(ex:Gender) = 1">
				The element Person should have the child elements Name and
				Gender.
			</assert>
			<assert test="ex:*[1] = ex:Name">
				The element Name must appear before element
				Gender.
			</assert>
		</rule>
	</pattern>
	<pattern>
		<title>Check co-occurrence constraints</title>
		<rule context="ex:Person">
			<assert test="(@Title = 'Mr' and ex:Gender = 'Male') or @Title != 'Mr'">
				If the Title is "Mr" then the gender of the
				person must be "Male".
			</assert>
		</rule>
	</pattern>
</schema> 