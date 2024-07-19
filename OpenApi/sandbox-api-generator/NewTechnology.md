# Research and development (R&D methodology suggestion)

R&D refers to innovative activities in developing new thinks or improving existing products. It could be
driven by a technical aging, a problem solving needs or business requirements.

### Motivation
I've seen a company's reality, and I'd like to suggest some improvements for this area. I guess,
follow base suggested steps could prevent a lot of disappointment from the R&D effort, and should
help to integrate the good ideas cross many company solutions. 

## Idea
Idea is a base of any innovations. But we have to define the specification. It means what problem
we want to solve or what behavior we want to change.
#### Realization
Often by brainstorming with specific output, or by whitePaper or some draft specification.
#### Outputs
* specify expectations -> targets
* specify acceptance -> base solution parameters (e.g. performance or easy to use)
* named content for PoC -> exact tasks with descriptions for PoC

## Proof of concept (PoC)
PoC is a realization of a certain method or idea in order to demonstrate its feasibility, or 
a demonstration in principle with the aim of verifying that some concept or theory has practical 
potential.
#### Preconditions
* the outputs from [Idea](#idea)
#### Realization
* a separate project for a realization
* own solution implementation
* a demonstration all [Idea](#idea) expectations
#### Outputs
* the solution descriptions (technology, methodology, ...)
* a physical demonstration
* a report for every acceptance criteria
* a report for found limits and minimal requirements
* an implementation risk naming
#### Notes
> To integrate a found solution with a product as an PoC is not a good idea. PoC's target is
> a preparation of some base isolated solution to be able make an independent reports about
> requirements or limitations for every potential products in future (e.g. perf. tests, dependencies, etc.) 

> To advocate a solution is good to add some comparison with alternative solutions. It could help to
> change or to define new requirements for the PoC during realization time.

## Playground
A lot of companies ignore this necessary step, and a final price is terrible. The technology
innovations are important for every leaders, and often define a line between success and fail.
It brings a competitive advantage what is necessary in every market area. 
#### Targets
* easy knowledge propagation - newbie training improvement
* terminology/dictionary synchronization - all teammates will use same terminology, so it will improve communication efficiency
* template for isolation testing new approaches or potential bug fixing
* knowledge holder - any technology problem solution should be documented here (it could follow version by original used technology)
#### Characteristics
* empty unnecessary isolated runnable project focused to the new technology only (e.g. jar or springBoot)
-> minimal working solution requirements definition
* very well documented every piece of code (what, why, how it is working ...)
* good to have there the practice recommendations
* good to have there idioms with explanation
* good to have description for all found problems and limitations
* README.md overview with references (e.g. version, home documentation, inspirations etc.)
* base test suite to demonstrate working
* description how to use, when to use and how to test it
* implemented reference for projects
#### Preconditions
* accepted [PoC](#proof-of-concept-poc)
#### Realization
* transform [PoC](#proof-of-concept-poc) - simplify it as much as is possible
* add code explanation and documentation
* add README.md overview (about solution and realization with the cross-references)
#### How to use PoC
######Newbie
> To play and to understand what is going on. Good to define a simple task for solving as a training
> point. The playground project could be maintenance by newbies (e.g. version increase ...) I guess,
> it could return the invested money back very soon.

######Template
> Every project could have unique setup, and it could bring a problem with some unnecessary changes.
> So some follower could use it as a template to prototype his solution without harming anything else.
> 
> It could help by investigation of some possible bugs too. Because isolation should negate other
> invisible impacts and programmer could make the changes and investigated combination quickly.

## Product integration
It is a final task to bring any innovations to the production. [Poc](#proof-of-concept-poc) and 
[Playground](#playground) help with define exact needed steps, define risks and hold knowledge for newbies.



