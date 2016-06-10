# CRaSH extensions
Add files here that are needed by crash shell to extend exsiting functionality.

Here are some ways you could extend CRaSH

## Providing your own type:
CRaSH uses the ServiceLoader discovery mechanism to discover custom types.
Custom types are implemented by a org.crsh.cli.type.ValueType subclass and implement its parse method:

Reference: [here](http://www.crashub.org/1.3/reference.html#_parameter_types)
